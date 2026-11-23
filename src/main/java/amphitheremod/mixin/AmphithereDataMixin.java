package amphitheremod.mixin;

import amphitheremod.config.AmphiConfig;
import amphitheremod.util.AmphiBreedRules;
import amphitheremod.util.EnumAmphiType;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Adds this mod's data to Ice and Fire's amphithere: gender, special variant, wing pattern, soul bond, stamina and COTH
 * immunity. Everything is synced entity data (clients render from it) and saved with the same NBT keys as 1.12.2, so
 * the data reads the same way. Also rolls the extra variants, gender, specials and wing pattern when one spawns.
 */
@Mixin(EntityAmphithere.class)
public abstract class AmphithereDataMixin extends TamableAnimal implements IAmphithereData {
    // Defined from inside EntityAmphithere's own static init (this mixin merges into it), which keeps vanilla's
    // "defineId called from another class" check happy
    @Unique
    private static final EntityDataAccessor<Boolean> amphiMod$FEMALE = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<String> amphiMod$SPECIAL = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.STRING);
    @Unique
    private static final EntityDataAccessor<String> amphiMod$WING_PATTERN = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.STRING);
    @Unique
    private static final EntityDataAccessor<Boolean> amphiMod$BOUND = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Float> amphiMod$MAX_STAMINA = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.FLOAT);
    @Unique
    private static final EntityDataAccessor<Float> amphiMod$STAMINA = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.FLOAT);
    @Unique
    private static final EntityDataAccessor<Integer> amphiMod$STAMINA_COOLDOWN = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> amphiMod$COTH_IMMUNE = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final ResourceLocation amphiMod$COTH = new ResourceLocation("epca", "coth");
    /** Bloody Cookie cost: max health is divided by this (about 10% less), same as 1.12.2. */
    @Unique
    private static final double amphiMod$COTH_HEALTH_DIVISOR = 1.1111;

    protected AmphithereDataMixin(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void amphiMod$defineData(CallbackInfo ci) {
        float maxStamina = AmphiConfig.SPEC.isLoaded() ? AmphiConfig.MAX_STAMINA.get().floatValue() : 175.0F;
        this.entityData.define(amphiMod$FEMALE, false);
        this.entityData.define(amphiMod$SPECIAL, "");
        this.entityData.define(amphiMod$WING_PATTERN, "");
        this.entityData.define(amphiMod$BOUND, false);
        this.entityData.define(amphiMod$MAX_STAMINA, maxStamina);
        this.entityData.define(amphiMod$STAMINA, maxStamina);
        this.entityData.define(amphiMod$STAMINA_COOLDOWN, 0);
        this.entityData.define(amphiMod$COTH_IMMUNE, false);
    }

    // Ice and Fire only rolls its 5 variants; roll from all of this mod's (normal, rare, gem, 1% rainbow) instead
    @Redirect(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;setVariant(I)V", remap = false))
    private void amphiMod$rollVariant(EntityAmphithere amphi, int variant) {
        amphi.setVariant(AmphiBreedRules.rollVariant(amphi.getRandom(), false));
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void amphiMod$onSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data,
                                  @Nullable CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        amphiMod$setFemale(this.random.nextBoolean());
        if (AmphiConfig.ENABLE_SHIVAXI.get() && this.random.nextInt(AmphiConfig.SHIVAXI_CHANCE.get()) == 0) {
            amphiMod$applySpecial("Shivaxi", true);
        } else if (AmphiConfig.ENABLE_BLACK_EAGLE.get() && this.random.nextInt(AmphiConfig.BLACK_EAGLE_CHANCE.get()) == 0) {
            amphiMod$applySpecial("Black Eagle", true);
        }
        // 1 in 10 wild spawns get a wing pattern from the ones their variant allows
        if (AmphiConfig.ENABLE_WING_PATTERNS.get() && this.random.nextInt(10) == 0) {
            EnumAmphiType.WingPattern[] patterns = EnumAmphiType.fromOrdinal(((EntityAmphithere) (Object) this).getVariant()).getWingPattern();
            EnumAmphiType.WingPattern pattern = patterns[this.random.nextInt(patterns.length)];
            amphiMod$setWingPattern(pattern == EnumAmphiType.WingPattern.NONE ? "" : pattern.name());
        }
    }

    /** Shivaxi / Black Eagle: their variant, always male, and their own health and damage. */
    @Unique
    private void amphiMod$applySpecial(String special, boolean heal) {
        amphiMod$setSpecialVariant(special);
        amphiMod$setFemale(false);
        ((EntityAmphithere) (Object) this).setVariant((special.equals("Shivaxi") ? EnumAmphiType.SHIVAXI : EnumAmphiType.BLACKEAGLE).ordinal());
        amphiMod$applyStats();
        if (heal) {
            this.setHealth(this.getMaxHealth());
        }
    }

    /**
     * Sets max health and attack from scratch: the special variant's stats or Ice and Fire's config values, then the
     * Bloody Cookie's health cost. Absolute rather than relative so running it twice never stacks the cost.
     */
    @Unique
    private void amphiMod$applyStats() {
        String special = amphiMod$getSpecialVariant();
        double health = IafConfig.amphithereMaxHealth;
        double damage = IafConfig.amphithereAttackStrength;
        if (special.equals("Shivaxi")) {
            health = AmphiConfig.SHIVAXI_HEALTH.get();
            damage = AmphiConfig.SHIVAXI_DAMAGE.get();
        } else if (special.equals("Black Eagle")) {
            health = AmphiConfig.BLACK_EAGLE_HEALTH.get();
            damage = AmphiConfig.BLACK_EAGLE_DAMAGE.get();
        }
        if (amphiMod$isCothImmune()) {
            health /= amphiMod$COTH_HEALTH_DIVISOR;
        }
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damage);
    }

    // Ice and Fire resets max health and attack to its config whenever it (re)applies its config, including on load;
    // put this mod's stats back on top
    @Inject(method = "setConfigurableAttributes", at = @At("TAIL"), remap = false)
    private void amphiMod$reapplyStats(CallbackInfo ci) {
        if (!amphiMod$getSpecialVariant().isEmpty() || amphiMod$isCothImmune()) {
            amphiMod$applyStats();
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void amphiMod$save(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("Gender", amphiMod$isFemale());
        tag.putString("SpecialVariant", amphiMod$getSpecialVariant());
        tag.putString("WingPattern", amphiMod$getWingPattern());
        tag.putBoolean("Bounded", amphiMod$isBound());
        tag.putFloat("StaminaMax", amphiMod$getMaxStamina());
        tag.putFloat("Stamina", amphiMod$getStamina());
        tag.putInt("StaminaRegenerationCooldown", amphiMod$getStaminaCooldown());
        tag.putBoolean("CothImmunie", amphiMod$isCothImmune());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void amphiMod$load(CompoundTag tag, CallbackInfo ci) {
        // Amphitheres from before this mod was added get a random gender
        amphiMod$setFemale(tag.contains("Gender") ? tag.getBoolean("Gender") : this.random.nextBoolean());
        amphiMod$setWingPattern(tag.getString("WingPattern"));
        amphiMod$setBound(tag.getBoolean("Bounded"));
        amphiMod$setCothImmune(tag.getBoolean("CothImmunie") || getPersistentData().getBoolean("CothImmunie"));
        // Ice and Fire's readAdditionalSaveData just reset max health and damage to its config values (before this data
        // was read), so put the special stats and the Bloody Cookie's cost back. Health itself was already loaded.
        String special = tag.getString("SpecialVariant");
        if (special.equals("Shivaxi") || special.equals("Black Eagle")) {
            amphiMod$applySpecial(special, false);
        } else {
            amphiMod$applyStats();
        }
        if (tag.contains("StaminaMax")) {
            amphiMod$setMaxStamina(tag.getFloat("StaminaMax"));
        }
        amphiMod$setStamina(tag.contains("Stamina") ? tag.getFloat("Stamina") : amphiMod$getMaxStamina());
        amphiMod$setStaminaCooldown(tag.getInt("StaminaRegenerationCooldown"));
    }

    @Override
    public void amphiMod$makeCothImmune() {
        amphiMod$setCothImmune(true);
        amphiMod$applyStats();
        this.setHealth(this.getMaxHealth());
    }

    // Keeps COTH off immune amphitheres
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void amphiMod$removeCoth(CallbackInfo ci) {
        if (!this.level().isClientSide && amphiMod$isCothImmune() && this.tickCount % 10 == 0) {
            MobEffect coth = ForgeRegistries.MOB_EFFECTS.getValue(amphiMod$COTH);
            if (coth != null && this.hasEffect(coth)) {
                this.removeEffect(coth);
            }
        }
    }

    @Override
    public boolean amphiMod$isFemale() {
        return this.entityData.get(amphiMod$FEMALE);
    }

    @Override
    public void amphiMod$setFemale(boolean female) {
        this.entityData.set(amphiMod$FEMALE, female);
    }

    @Override
    public String amphiMod$getSpecialVariant() {
        return this.entityData.get(amphiMod$SPECIAL);
    }

    @Override
    public void amphiMod$setSpecialVariant(String specialVariant) {
        this.entityData.set(amphiMod$SPECIAL, specialVariant);
    }

    @Override
    public String amphiMod$getWingPattern() {
        return this.entityData.get(amphiMod$WING_PATTERN);
    }

    @Override
    public void amphiMod$setWingPattern(String wingPattern) {
        this.entityData.set(amphiMod$WING_PATTERN, wingPattern);
    }

    @Override
    public boolean amphiMod$isBound() {
        return this.entityData.get(amphiMod$BOUND);
    }

    @Override
    public void amphiMod$setBound(boolean bound) {
        this.entityData.set(amphiMod$BOUND, bound);
    }

    @Override
    public float amphiMod$getStamina() {
        return this.entityData.get(amphiMod$STAMINA);
    }

    @Override
    public void amphiMod$setStamina(float stamina) {
        this.entityData.set(amphiMod$STAMINA, Mth.clamp(stamina, 0.0F, amphiMod$getMaxStamina()));
    }

    @Override
    public float amphiMod$getMaxStamina() {
        return this.entityData.get(amphiMod$MAX_STAMINA);
    }

    @Override
    public void amphiMod$setMaxStamina(float maxStamina) {
        this.entityData.set(amphiMod$MAX_STAMINA, maxStamina);
    }

    @Override
    public int amphiMod$getStaminaCooldown() {
        return this.entityData.get(amphiMod$STAMINA_COOLDOWN);
    }

    @Override
    public void amphiMod$setStaminaCooldown(int cooldown) {
        this.entityData.set(amphiMod$STAMINA_COOLDOWN, cooldown);
    }

    @Override
    public boolean amphiMod$isCothImmune() {
        return this.entityData.get(amphiMod$COTH_IMMUNE);
    }

    @Override
    public void amphiMod$setCothImmune(boolean immune) {
        this.entityData.set(amphiMod$COTH_IMMUNE, immune);
    }
}
