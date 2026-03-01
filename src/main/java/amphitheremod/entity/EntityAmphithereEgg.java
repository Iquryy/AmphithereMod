package amphitheremod.entity;

import amphitheremod.config.ConfigHandler;
import amphitheremod.handlers.ModItemRegistry;
import amphitheremod.util.IAmphithereData;
import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

public class EntityAmphithereEgg extends EntityLiving {
    private static final DataParameter<Integer> AMPHITHERE_TYPE;
    private static final DataParameter<Integer> AMPHITHERE_AGE;
    private static final DataParameter<Boolean> IS_WARM;

    public EntityAmphithereEgg(World worldIn) {
        super(worldIn);
        this.setSize(0.25F, 0.35F);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
    }

    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("Variant", this.getType().ordinal());
        tag.setInteger("AmphithereAge", this.getAmphithereAge());
    }

    public void readEntityFromNBT(@NotNull NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setType(EnumAmphiType.values()[tag.getInteger("Variant")]);
        this.setAmphithereAge(tag.getInteger("AmphithereAge"));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(AMPHITHERE_TYPE, 0);
        this.getDataManager().register(AMPHITHERE_AGE, 0);
        this.getDataManager().register(IS_WARM, false);
    }

    public boolean isWarm() {
        return this.getDataManager().get(IS_WARM);
    }

    public void setWarm(boolean warm) {
        this.getDataManager().set(IS_WARM, warm);
    }

    public EnumAmphiType getType() {
        return EnumAmphiType.values()[this.getDataManager().get(AMPHITHERE_TYPE)];
    }

    public void setType(EnumAmphiType newtype) {
        this.getDataManager().set(AMPHITHERE_TYPE, newtype.ordinal());
    }

    public boolean isEntityInvulnerable(DamageSource i) {
        return i.getTrueSource() != null;
    }

    public int getAmphithereAge() {
        return this.getDataManager().get(AMPHITHERE_AGE);
    }

    public void setAmphithereAge(int i) {
        this.getDataManager().set(AMPHITHERE_AGE, i);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setAir(200);
        if (this.world.isRemote) return;
        boolean isCurrentlyWarm = this.meetsEggCondition(this, this.getPosition());
        this.setWarm(isCurrentlyWarm);
        if (isCurrentlyWarm)
            this.setAmphithereAge(this.getAmphithereAge() + 1);
        if (this.meetsEggCondition(this, this.getPosition()))
            this.setAmphithereAge(this.getAmphithereAge() + 1);
        if (this.getAmphithereAge() > ConfigHandler.amphithereEgg.amphithereEggHatchTime)
            this.hatchEgg();
    }

    private void hatchEgg() {
        if (this.world.isRemote) return;
        EntityAmphithere amphithere = new EntityAmphithere(this.world);
        amphithere.setPosition(this.posX, this.posY, this.posZ);
        amphithere.setVariant(this.getType().ordinal());
        amphithere.setGrowingAge(-24000);
        IAmphithereData data = (IAmphithereData) amphithere;
        data.amphiMod_master$setGender(this.world.rand.nextBoolean());
        if(this.hasCustomName()) amphithere.setCustomNameTag(this.getCustomNameTag());
        this.world.spawnEntity(amphithere);
        this.setDead();
    }

    public boolean isAIDisabled() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.world.isRemote || this.isDead) return false;
        if (!(source.isCreativePlayer()) && !(source == DamageSource.FALL)) {
            Item correctEggItem = ModItemRegistry.AMPHITHERE_EGGS.get(this.getType());
            if (correctEggItem != null)
                this.entityDropItem(new ItemStack(correctEggItem), 0.0F);
        }
        if (source == DamageSource.FALL) {
            if (ConfigHandler.amphithereEgg.eggCrackFallDamage) {
            } else {
                Item correctEggItem = ModItemRegistry.AMPHITHERE_EGGS.get(this.getType());
                if (correctEggItem != null)
                    this.entityDropItem(new ItemStack(correctEggItem), 0.0F);
            }
        }

        this.setDead();
        return true;
    }

    public boolean meetsEggCondition(EntityAmphithereEgg egg, BlockPos pos) {
        if (ConfigHandler.amphithereEgg.needOnLeaf) {
            boolean isTouchingLeaves = egg.world.getBlockState(pos.down()).getMaterial() == Material.LEAVES;
            if (!isTouchingLeaves) return false;
        }

        if (ConfigHandler.amphithereEgg.warmBiome) {
            Biome currentBiome = egg.world.getBiome(pos);
            float temperature = currentBiome.getTemperature(pos);
            return !(temperature < 0.8F);
        }
        return true;
    }

    protected boolean canDespawn() {
        return false;
    }

    static {
        AMPHITHERE_TYPE = EntityDataManager.createKey(EntityAmphithereEgg.class, DataSerializers.VARINT);
        AMPHITHERE_AGE = EntityDataManager.createKey(EntityAmphithereEgg.class, DataSerializers.VARINT);
        IS_WARM = EntityDataManager.createKey(EntityAmphithereEgg.class, DataSerializers.BOOLEAN);
    }

}

