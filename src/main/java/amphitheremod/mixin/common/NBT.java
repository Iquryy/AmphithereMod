package amphitheremod.mixin.common;

import amphitheremod.util.EnumAmphiType;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import static amphitheremod.config.ConfigHandler.*;
import static amphitheremod.util.AmphiBreedRules.rollVariant;

@Mixin(EntityAmphithere.class)
public abstract class NBT extends EntityAnimal implements IAmphithereData {

    public NBT(World worldIn) {
        super(worldIn);
    }

    @Unique private static DataParameter<Boolean> DATA_GENDER;
    @Unique private static DataParameter<String> DATA_SPECIAL_VARIANT;
    @Unique private static DataParameter<String> DATA_WING_PATTERN;
    @Unique private static DataParameter<Boolean> DATA_BOUNDED;
    @Unique private static DataParameter<Float> DATA_STAMINA_MAX;
    @Unique private static DataParameter<Float> DATA_STAMINA;
    @Unique private static DataParameter<Integer> DATA_STAMINA_REG_CD;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void amphimod_createGenderDataParam(CallbackInfo ci) {
        DATA_GENDER = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.BOOLEAN);
        DATA_SPECIAL_VARIANT = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.STRING);
        DATA_WING_PATTERN = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.STRING);
        DATA_BOUNDED = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.BOOLEAN);
        DATA_STAMINA_MAX = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.FLOAT);
        DATA_STAMINA = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.FLOAT);
        DATA_STAMINA_REG_CD = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.VARINT);
    }

    @Inject(method = "entityInit", at = @At("TAIL"))
    private void entityInit(CallbackInfo ci) {
        this.getDataManager().register(DATA_GENDER, false);
        this.getDataManager().register(DATA_SPECIAL_VARIANT, "");
        this.getDataManager().register(DATA_WING_PATTERN, "");
        this.getDataManager().register(DATA_BOUNDED, false);
        this.getDataManager().register(DATA_STAMINA_MAX, amphiStamina.maxStamina);
        this.getDataManager().register(DATA_STAMINA, amphiStamina.maxStamina);
        this.getDataManager().register(DATA_STAMINA_REG_CD, 0);
        if(!this.getEntityData().getBoolean("CothImmunie"))
            this.getEntityData().setBoolean("CothImmunie", false);
    }



    @Inject(method = "onInitialSpawn", at = @At("TAIL"))
    private void onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata, CallbackInfoReturnable<IEntityLivingData> cir) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        this.amphiMod_master$setGender(this.getRNG().nextBoolean());

        if(shivaxi.enableShivaxiAmphithere) {
            if (this.getRNG().nextInt(shivaxi.shivaxiAmphithereChance) == 1 || this.amphiMod_master$getSpecialVariant().equals("Shivaxi"))
                this.amphiMod_master$applyShivaxiStats();
        }

        if(blackEagle.enableBlackEagleAmphithere) {
            if (this.getRNG().nextInt(blackEagle.blackEagleAmphithereChance) == 1 || this.amphiMod_master$getSpecialVariant().equals("BlackEagle"))
                this.amphiMod_master$applyBlackEagleStats();
        }

        if(!(general.enableWingPatterns)) return;
        EnumAmphiType.WingPattern[] wingPatterns = EnumAmphiType.values()[amphi.getVariant()].getWingPattern();
        if (wingPatterns.length > 0) {
            if(!(amphi.getRNG().nextInt(10) == 5)) return;
            String initialPattern = String.valueOf(wingPatterns[this.getRNG().nextInt(wingPatterns.length)]);
            this.amphiMod_master$setWingPattern(initialPattern);
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void writeEntityToNBT(NBTTagCompound compound, CallbackInfo ci) {
        compound.setBoolean("Gender", this.amphiMod_master$getGender());
        compound.setString("SpecialVariant", this.amphiMod_master$getSpecialVariant());
        compound.setString("WingPattern", this.amphiMod_master$getWingPattern());
        compound.setBoolean("Bounded", this.amphiMod_master$getBounded());
        compound.setFloat("StaminaMax", this.amphiMod_master$getMaxStamina());
        compound.setFloat("Stamina", this.amphiMod_master$getStamina());
        compound.setInteger("StaminaRegenerationCooldown", this.amphiMod_master$getStaminaCD());
        this.getEntityData().setBoolean("CothImmunie", this.getEntityData().getBoolean("CothImmunie"));
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void readEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;

        if (compound.hasKey("Gender"))
            this.amphiMod_master$setGender(compound.getBoolean("Gender"));
        else
            this.amphiMod_master$setGender(this.getRNG().nextBoolean());

        String specialVariant = compound.getString("SpecialVariant");
        this.amphiMod_master$setSpecialVariant(specialVariant);

        if (compound.hasKey("WingPattern"))
            this.amphiMod_master$setWingPattern(compound.getString("WingPattern"));

        if (specialVariant.equals("Shivaxi"))
            amphiMod_master$applyShivaxiStats();

        if (specialVariant.equals("Black Eagle"))
            amphiMod_master$applyBlackEagleStats();

        if (compound.hasKey("Variant"))
            amphi.setVariant(compound.getInteger("Variant"));
        else if (specialVariant.isEmpty())
            amphi.setVariant(rollVariant(amphi.getRNG(), false));

        if (compound.hasKey("Bounded"))
            this.amphiMod_master$setBounded(compound.getBoolean("Bounded"));
        else
            this.amphiMod_master$setBounded(false);

        if (compound.hasKey("StaminaMax"))
            this.amphiMod_master$setMaxStamina(compound.getFloat("StaminaMax"));
        else
            this.amphiMod_master$setStamina(this.amphiMod_master$getMaxStamina());

        if (compound.hasKey("Stamina")) {
            this.amphiMod_master$setStamina(compound.getFloat("Stamina"));
        }
        else
            this.amphiMod_master$setStamina(this.amphiMod_master$getMaxStamina());

        if (compound.hasKey("StaminaRegenerationCooldown"))
            this.amphiMod_master$setStaminaCD(compound.getInteger("StaminaRegenerationCooldown"));
        else
            this.amphiMod_master$setStaminaCD(0);

        if (this.getEntityData().getBoolean("CothImmunie"))
            this.getEntityData().setBoolean("CothImmunie", this.getEntityData().getBoolean("CothImmunie"));
        else
            this.getEntityData().setBoolean("CothImmunie", false);
    }

    @Unique
    private void amphiMod_master$applyShivaxiStats() {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        this.amphiMod_master$setSpecialVariant("Shivaxi");
        this.amphiMod_master$setGender(false);
        amphi.setVariant(EnumAmphiType.getIntFromEnum(EnumAmphiType.SHIVAXI));
        amphi.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shivaxi.shivaxiAmphithereHealth);
        amphi.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(shivaxi.shivaxiAmphithereDamage);
        amphi.setHealth((float) amphi.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
        amphi.setHealth(amphi.getMaxHealth());
    }

    @Unique
    private void amphiMod_master$applyBlackEagleStats() {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        this.amphiMod_master$setSpecialVariant("Black Eagle");
        this.amphiMod_master$setGender(false);
        amphi.setVariant(EnumAmphiType.getIntFromEnum(EnumAmphiType.BLACKEAGLE));
        amphi.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(blackEagle.blackEagleAmphithereHealth);
        amphi.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(blackEagle.blackEagleAmphithereDamage);
        amphi.setHealth((float) amphi.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
        amphi.setHealth(amphi.getMaxHealth());
    }

    @Override
    public void amphiMod_master$setStamina(float stamina) {
        float maxStamina = this.amphiMod_master$getMaxStamina();
        float clampedStamina = MathHelper.clamp(stamina, 0, maxStamina);
        this.getDataManager().set(DATA_STAMINA, clampedStamina);
    }

    @Override
    public float amphiMod_master$getMaxStamina() { return this.getDataManager().get(DATA_STAMINA_MAX); }

    @Override
    public void amphiMod_master$setMaxStamina(float maxStamina) { this.getDataManager().set(DATA_STAMINA_MAX, maxStamina); }

    @Override
    public int amphiMod_master$getStaminaCD() { return this.getDataManager().get(DATA_STAMINA_REG_CD); }

    @Override
    public void amphiMod_master$setStaminaCD(int cd) { this.getDataManager().set(DATA_STAMINA_REG_CD, cd); }

    @Override
    public float amphiMod_master$getStamina() { return this.getDataManager().get(DATA_STAMINA); }

    @Override
    public boolean amphiMod_master$getGender() {
        return this.getDataManager().get(DATA_GENDER);
    }

    @Override
    public void amphiMod_master$setGender(boolean gender) {
        this.getDataManager().set(DATA_GENDER, gender);
    }

    @Override
    public String amphiMod_master$getSpecialVariant() {
        return this.getDataManager().get(DATA_SPECIAL_VARIANT);
    }

    @Override
    public void amphiMod_master$setSpecialVariant(String specialVariant) {
        this.getDataManager().set(DATA_SPECIAL_VARIANT, specialVariant);
    }

    @Override
    public String amphiMod_master$getWingPattern() {
        return this.getDataManager().get(DATA_WING_PATTERN);
    }

    @Override
    public void amphiMod_master$setWingPattern(String wingPattern) {
        this.getDataManager().set(DATA_WING_PATTERN, wingPattern);
    }

    @Override
    public boolean amphiMod_master$getBounded() {
        return this.getDataManager().get(DATA_BOUNDED);
    }

    @Override
    public void amphiMod_master$setBounded(boolean bounded) {
        this.getDataManager().set(DATA_BOUNDED, bounded);
    }
}