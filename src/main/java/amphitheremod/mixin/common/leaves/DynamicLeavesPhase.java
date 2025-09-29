package amphitheremod.mixin.common.leaves;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockDynamicLeaves.class)
public abstract class DynamicLeavesPhase {
    @Inject(method = "onEntityCollision", at = @At(value = "HEAD", remap = false), cancellable = true)
    public void amphithereLeavesCollision(World world, BlockPos pos, IBlockState state, Entity entity, CallbackInfo ci) {
        if (entity instanceof EntityAmphithere) ci.cancel();
    }
}