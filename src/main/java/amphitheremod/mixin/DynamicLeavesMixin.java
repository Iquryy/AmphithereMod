package amphitheremod.mixin;

import amphitheremod.config.AmphiConfig;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Dynamic Trees leaves slow and hurt entities moving through them and can be solid to them. Amphitheres fly through
 * leaves in Ice and Fire, so they pass Dynamic Trees leaves freely too. Only applied when Dynamic Trees is installed
 * (mixin.AmphiMixinPlugin). Method names are given as both the dev and the SRG name with remap off, since there's no
 * compile-time Dynamic Trees dependency for the refmap to map them from.
 */
@Pseudo
@Mixin(targets = "com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock", remap = false)
public abstract class DynamicLeavesMixin {
    @Inject(method = {"entityInside", "m_7892_"}, at = @At("HEAD"), cancellable = true, require = 0)
    private void amphiMod$noSlowdown(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof EntityAmphithere && AmphiConfig.PASS_DYNAMIC_LEAVES.get() && !AmphiConfig.CANT_PASS_LEAVES.get()) {
            ci.cancel();
        }
    }

    @Inject(method = "isEntityPassable", at = @At("HEAD"), cancellable = true, require = 0)
    private void amphiMod$passable(CollisionContext context, CallbackInfoReturnable<Boolean> cir) {
        if (context instanceof EntityCollisionContext ctx && ctx.getEntity() instanceof EntityAmphithere
                && AmphiConfig.PASS_DYNAMIC_LEAVES.get() && !AmphiConfig.CANT_PASS_LEAVES.get()) {
            cir.setReturnValue(true);
        }
    }
}
