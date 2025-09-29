package amphitheremod.mixin.common.leaves;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EntityAmphithere.class, remap = false)
public class LeavesPhaseChange {

    @ModifyReturnValue(method = "onLeaves", at = @At(value = "TAIL", remap = false))
    boolean onLeaves(boolean original){
        return false;
    }

    @ModifyReturnValue(method = "canPhaseThroughBlock", at = @At(value = "TAIL", remap = false))
    boolean canPhaseThroughBlock(boolean original){
        return false;
    }
}