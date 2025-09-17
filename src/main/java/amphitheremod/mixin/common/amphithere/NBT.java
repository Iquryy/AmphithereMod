package amphitheremod.mixin.common.amphithere;

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
import static amphitheremod.util.AmphiBreedingRules.AmphiBreedRules.rollVariant;

@Mixin(EntityAmphithere.class)
public abstract class NBT extends EntityAnimal implements IAmphithereData {
    public NBT(World worldIn) {
        super(worldIn);
    }

    @Unique private static DataParameter<Boolean> DATA_GENDER;
    @Unique private static DataParameter<Boolean> DATA_IS_SHIVAXI;
    @Unique private static DataParameter<Boolean> DATA_BOUNDED;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void amphimod_createGenderDataParam(CallbackInfo ci) {
        DATA_GENDER = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.BOOLEAN);
        DATA_IS_SHIVAXI = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.BOOLEAN);
        DATA_BOUNDED = EntityDataManager.createKey(EntityAmphithere.class, DataSerializers.BOOLEAN);
    }

    @Override
    public boolean amphiMod_master$getGender() {
        return this.getDataManager().get(DATA_GENDER);
    }

    @Override
    public void amphiMod_master$setGender(boolean gender) {
        this.getDataManager().set(DATA_GENDER, gender);
    }

    @Override
    public boolean amphiMod_master$getShivaxi() {
        return this.getDataManager().get(DATA_IS_SHIVAXI);
    }

    @Override
    public void amphiMod_master$setShivaxi(boolean shivaxi) {
        this.getDataManager().set(DATA_IS_SHIVAXI, shivaxi);
    }

    @Override
    public boolean amphiMod_master$getBounded() {
        return this.getDataManager().get(DATA_BOUNDED);
    }

    @Override
    public void amphiMod_master$setBounded(boolean bounded) {
        this.getDataManager().set(DATA_BOUNDED, bounded);
    }

    @Inject(method = "entityInit", at = @At("TAIL"))
    private void entityInit(CallbackInfo ci) {
        this.getDataManager().register(DATA_GENDER, false);
        this.getDataManager().register(DATA_IS_SHIVAXI, false);
        this.getDataManager().register(DATA_BOUNDED, false);
    }

    @Inject(method = "onInitialSpawn", at = @At("TAIL"))
    private void onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata, CallbackInfoReturnable<IEntityLivingData> cir) {
        this.amphiMod_master$setGender(this.getRNG().nextBoolean());

        if(shivaxi.enableShivaxiAmphithere) {
            if (this.getRNG().nextInt(shivaxi.shivaxiAmphithereChance) == 1 || this.amphiMod_master$getShivaxi())
                this.amphiMod_master$applyShivaxiStats();
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void writeEntityToNBT(NBTTagCompound compound, CallbackInfo ci) {
        compound.setBoolean("Gender", this.amphiMod_master$getGender());
        compound.setBoolean("Shivaxi", this.amphiMod_master$getShivaxi());
        compound.setBoolean("Bounded", this.amphiMod_master$getBounded());
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void readEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;

        // GENDER
        if (compound.hasKey("Gender")) {
            this.getDataManager().set(DATA_GENDER, compound.getBoolean("Gender"));
        } else {
            this.amphiMod_master$setGender(this.getRNG().nextBoolean());
        }

        // SHIVAXI
        boolean isShivaxiNBT = false;
        if (compound.hasKey("Shivaxi")) {
            isShivaxiNBT = compound.getBoolean("Shivaxi");
            this.amphiMod_master$setShivaxi(isShivaxiNBT);
            if (isShivaxiNBT) {
                amphiMod_master$applyShivaxiStats(); // This sets the variant to SHIVAXI
            }
        } else {
            this.amphiMod_master$setShivaxi(false);
        }

        // VARIANT
        // Prioritize NBT 'Variant' tag if present.
        if (compound.hasKey("Variant")) {
            amphi.setVariant(compound.getInteger("Variant"));
        } else if (!isShivaxiNBT) { // Only roll a variant if it's not a Shivaxi and no Variant NBT was provided
            amphi.setVariant(rollVariant(amphi.getRNG(), false));
        }

        // BOUNDED
        if (compound.hasKey("Bounded")) {
            this.getDataManager().set(DATA_BOUNDED, compound.getBoolean("Bounded"));
        } else {
            this.amphiMod_master$setBounded(false);
        }
    }

    @Unique
    private void amphiMod_master$applyShivaxiStats() {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        this.amphiMod_master$setShivaxi(true);
        this.amphiMod_master$setGender(false);
        amphi.setVariant(EnumAmphiType.getIntFromEnum(EnumAmphiType.SHIVAXI));
        amphi.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shivaxi.shivaxiAmphithereHealth);
        amphi.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(shivaxi.shivaxiAmphithereDamage);
    }
}