package amphitheremod.mixin.common.amphithere;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAmphithere.class)
public class LeafFix {

    @ModifyReturnValue(
            method = "onLeaves",
            at = @At("RETURN"),
            remap = false
    )
    public boolean amphiMod_infEntityAmphithere_onLeavesDisablePhasing(boolean original){
        return false;
    }

    @ModifyReturnValue(
            method = "canPhaseThroughBlock",
            at = @At("RETURN"),
            remap = false
    )
    public boolean amphiMod_infEntityAmphithere_canPhaseThroughBlockDisablePhasing(boolean original){
        return false;
    }
}
