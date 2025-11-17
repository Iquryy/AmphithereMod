package amphitheremod.mixin.common;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityAmphithere.class, remap = false)
public class OnHearFlute {
    @Inject(method = "onHearFlute", at = @At(value = "HEAD"))
    void flute(EntityPlayer player, CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (amphi.isTamed() && (!amphi.onGround) && !amphi.isBeingRidden())
            amphi.setCommand(2);
    }
}
