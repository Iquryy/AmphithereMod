package amphitheremod.mixin.common;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityAmphithere.class, remap = false)
public class OnHearFlute {

    @Shadow
    protected EntityAmphithere.FlightBehavior flightBehavior;

    @Inject(method = "onHearFlute", at = @At(value = "HEAD"), cancellable = true)
    void flute(EntityPlayer player, CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (amphi.isTamed() && (!amphi.onGround) && !amphi.isBeingRidden() && player == amphi.getOwner()) {
            amphi.isFallen = true;
            amphi.setCommand(2);
        }
        ci.cancel();
    }

    // While "fallen" (flute, or a wild one shot down by an arrow) Ice and Fire sets flightBehavior to NONE, but it
    // never sets it back after landing. A NONE amphithere with no rider is pulled down by 0.3 every tick, against
    // the 0.08 lift from flying, so every later flight sank fast in a permanent dive. Restore normal flight once
    // it is no longer fallen. Ice and Fire then switches WANDER to CIRCLE by itself for escort or home mode.
    @Inject(method = "onLivingUpdate", at = @At("HEAD"), remap = true)
    void amphithereMod$recoverFromFall(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!amphi.isFallen && this.flightBehavior == EntityAmphithere.FlightBehavior.NONE) {
            this.flightBehavior = EntityAmphithere.FlightBehavior.WANDER;
        }
    }
}