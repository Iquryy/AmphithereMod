package amphitheremod.entity;

import amphitheremod.config.AmphiConfig;
import amphitheremod.registry.AmphiItems;
import amphitheremod.util.EnumAmphiType;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * A laid amphithere egg. While it's "warm" (on leaves in a warm biome, by default) it ages, wobbles, and hatches into a
 * baby amphithere of its variant after the hatch time. Hitting it gives the egg item back; falling breaks it.
 */
public class EntityAmphithereEgg extends LivingEntity {
    private static final EntityDataAccessor<Integer> AMPHITHERE_TYPE = SynchedEntityData.defineId(EntityAmphithereEgg.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> AMPHITHERE_AGE = SynchedEntityData.defineId(EntityAmphithereEgg.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_WARM = SynchedEntityData.defineId(EntityAmphithereEgg.class, EntityDataSerializers.BOOLEAN);

    public EntityAmphithereEgg(EntityType<? extends EntityAmphithereEgg> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AMPHITHERE_TYPE, 0);
        this.entityData.define(AMPHITHERE_AGE, 0);
        this.entityData.define(IS_WARM, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", getVariantType().ordinal());
        tag.putInt("AmphithereAge", getAmphithereAge());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setVariantType(EnumAmphiType.fromOrdinal(tag.getInt("Variant")));
        setAmphithereAge(tag.getInt("AmphithereAge"));
    }

    public boolean isWarm() {
        return this.entityData.get(IS_WARM);
    }

    public EnumAmphiType getVariantType() {
        return EnumAmphiType.fromOrdinal(this.entityData.get(AMPHITHERE_TYPE));
    }

    public void setVariantType(EnumAmphiType type) {
        this.entityData.set(AMPHITHERE_TYPE, type.ordinal());
    }

    public int getAmphithereAge() {
        return this.entityData.get(AMPHITHERE_AGE);
    }

    public void setAmphithereAge(int age) {
        this.entityData.set(AMPHITHERE_AGE, age);
    }

    @Override
    public void tick() {
        super.tick();
        this.setAirSupply(200); // eggs don't drown
        if (level().isClientSide) {
            return;
        }
        boolean warm = meetsEggCondition(blockPosition());
        this.entityData.set(IS_WARM, warm);
        if (warm) {
            // One tick of age per warm tick (1.12.2 added two; see AmphiConfig.EGG_HATCH_TIME)
            setAmphithereAge(getAmphithereAge() + 1);
        }
        if (getAmphithereAge() > AmphiConfig.EGG_HATCH_TIME.get()) {
            hatchEgg();
        }
    }

    private void hatchEgg() {
        EntityAmphithere amphithere = IafEntityRegistry.AMPHITHERE.get().create(level());
        if (amphithere == null) {
            return;
        }
        amphithere.setPos(getX(), getY(), getZ());
        amphithere.setVariant(getVariantType().ordinal());
        amphithere.setAge(-24000);
        ((IAmphithereData) amphithere).amphiMod$setFemale(this.random.nextBoolean());
        if (hasCustomName()) {
            amphithere.setCustomName(getCustomName());
        }
        level().addFreshEntity(amphithere);
        discard();
    }

    /** Warm = on leaves and in a warm biome (each check can be turned off). */
    public boolean meetsEggCondition(BlockPos pos) {
        if (AmphiConfig.EGG_NEEDS_LEAVES.get() && !level().getBlockState(pos.below()).is(BlockTags.LEAVES)) {
            return false;
        }
        if (AmphiConfig.EGG_NEEDS_WARM_BIOME.get()) {
            return level().getBiome(pos).value().getBaseTemperature() >= 0.8F;
        }
        return true;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (level().isClientSide || isRemoved()) {
            return false;
        }
        // Any hit pops the egg back into an item, except explosions and (unless disabled) falling, which break it
        boolean fall = source.is(DamageTypeTags.IS_FALL);
        boolean dropItem = fall ? !AmphiConfig.EGG_CRACK_FALL.get() : !source.is(DamageTypeTags.IS_EXPLOSION);
        Item eggItem = AmphiItems.eggFor(getVariantType());
        if (dropItem && eggItem != null) {
            spawnAtLocation(new ItemStack(eggItem), 0.0F);
        }
        discard();
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
}
