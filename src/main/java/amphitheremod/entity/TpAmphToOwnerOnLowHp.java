package amphitheremod.entity;


import amphitheremod.config.ConfigHandler;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class TpAmphToOwnerOnLowHp { @SubscribeEvent
    public static void tpAmphToOwnerOnLowHp(LivingHurtEvent event) {
        if (ConfigHandler.general.tpAmphOnLowHp) {
            Entity entity = event.getEntity();
            if (entity instanceof EntityAmphithere) {
                EntityAmphithere amphi = (EntityAmphithere) entity;
                if (amphi.isTamed() && (!(amphi.isBeingRidden())))
                    if (amphi.getHealth() <= ConfigHandler.general.hpThresh) {
                        if (amphi.getOwner() == null) return;
                        int i = MathHelper.floor(amphi.getOwner().posX) - 2;
                        int j = MathHelper.floor(amphi.getOwner().posZ) - 2;
                        int k = MathHelper.floor(amphi.getOwner().getEntityBoundingBox().minY);
                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && isTeleportFriendlyBlock(amphi, i, j, k, l, i1)) {
                                    amphi.setLocationAndAngles(((float) (i + l) + 0.5F), k, ((float) (j + i1) + 0.5F), amphi.rotationYaw, amphi.rotationPitch);
                                    amphi.getNavigator().clearPath();
                                    return;
                                }
                            }
                        }
                    }
            }
        }
    }

    public static boolean isTeleportFriendlyBlock(EntityAmphithere amphi, int x, int z, int y, int xOffset, int zOffset) {
        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        IBlockState iblockstate = amphi.world.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(amphi.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(amphi) && amphi.world.isAirBlock(blockpos.up()) && amphi.world.isAirBlock(blockpos.up(2));
    }
}