package amphitheremod.mixin.modcompat.dynamictrees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAmphithere.class)
public abstract class EntityAmphithere_DynaBranchPassableMixin extends EntityTameable {

    // Not sure but probably costly to recalc every tick, hence cache
    @Unique
    BlockPos amphithereMod$cachedRootPos = null;

    public EntityAmphithere_DynaBranchPassableMixin(World world) {
        super(world);
    }

    @ModifyReturnValue(
            method = "onLeaves",
            at = @At("RETURN"),
            remap = false
    )
    public boolean amphiMod_infEntityAmphithere_onLeavesDynamicTreePart(boolean original, @Local IBlockState state){
        return original || amphithereMod$isTreeBranch(world, this.getPosition().down());
    }

    @ModifyReturnValue(
            method = "canPhaseThroughBlock",
            at = @At("RETURN"),
            remap = false
    )
    // Not using the IBlockState as only the RLC Fork uses it
    public boolean amphiMod_infEntityAmphithere_canPhaseThroughBlockDynamicTreePart(boolean original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos){
        return original || amphithereMod$isTreeBranch(world, pos);
    }

    /**
     *
     * @return Whether the block is a dynamic tree branch and not part of the tree trunk
     */
    @Unique
    public boolean amphithereMod$isTreeBranch(World world, BlockPos pos){
        if(!TreeHelper.isBranch(world.getBlockState(pos))) {
            amphithereMod$cachedRootPos = null;
            return false;
        }

        // Find where the tree trunk should be
        if(amphithereMod$cachedRootPos == null) {
            amphithereMod$cachedRootPos = TreeHelper.findRootNode(world, pos);
        }

        // Assume any branch above the root is part of the tree trunk
        return !(pos.getX() == amphithereMod$cachedRootPos.getX() && pos.getZ() == amphithereMod$cachedRootPos.getZ());
    }
}
