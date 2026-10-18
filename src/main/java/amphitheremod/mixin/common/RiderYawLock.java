package amphitheremod.mixin.common;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keeps a ridden amphithere facing where its rider looks.
 *
 * Ice and Fire only copies the rider's yaw in updatePassenger. In between, two things turn the body:
 * - its AI keeps running (follow, attack, hurt-by-target), and the move helper turns the body toward whatever
 *   path the AI set;
 * - vanilla's EntityLivingBase.onUpdate turns renderYawOffset toward the direction of motion after
 *   onLivingUpdate has set it.
 * Together that made the amphithere glance around as if trying to walk off on its own.
 */
@Mixin(EntityAmphithere.class)
public abstract class RiderYawLock {

    @Inject(method = "onUpdate", at = @At("TAIL"))
    private void amphithereMod$faceRiderLook(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        Entity rider = amphi.getControllingPassenger(); // only returns the owner, and only when tamed
        if (!(rider instanceof EntityPlayer)) return;

        if (!amphi.world.isRemote) {
            // Drop any path the AI set, so the move helper has nothing to turn toward.
            amphi.getNavigator().clearPath();
        }

        float yaw = rider.rotationYaw;
        amphi.rotationYaw = yaw;
        amphi.rotationYawHead = yaw;
        // renderYawOffset (the body you see) is eased toward this yaw on the client by AmphiAnimState, for smooth
        // turning with banking. Steering uses rotationYaw, so it still follows the rider instantly.
    }
}