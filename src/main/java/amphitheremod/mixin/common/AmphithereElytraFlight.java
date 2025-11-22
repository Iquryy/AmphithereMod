package amphitheremod.mixin.common;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//import static amphitheremod.proxy.CommonProxy.ELYTRA_DIVE_KEY;

@Mixin(EntityAmphithere.class)
public abstract class AmphithereElytraFlight extends Entity {
    public AmphithereElytraFlight(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false) @Final private static DataParameter<Byte> CONTROL_STATE;

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void replaceTravelWithElytra(float strafe, float vertical, float forward, CallbackInfo ci) {
        if (amphithereMod$edk()) {
            double radConversion = Math.PI / 180.0;
            float yawRad = (float) (this.rotationYaw * radConversion);
            float pitchRad = (float) (this.rotationPitch * radConversion);

            double yawcos = Math.cos(-yawRad - Math.PI);
            double yawsin = Math.sin(-yawRad - Math.PI);
            double pitchcos = Math.cos(pitchRad);
            double pitchsin = Math.sin(pitchRad);

            double lookX = yawsin * -pitchcos;
            double lookZ = yawcos * -pitchcos;

            double hvel = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            double hlook = pitchcos;
            double sqrpitchcos = pitchcos * pitchcos;

            this.motionY += -0.08 + sqrpitchcos * 0.06;

            if (this.motionY < 0 && hlook > 0) {
                double yacc = this.motionY * -0.1 * sqrpitchcos;
                this.motionY += yacc;
                this.motionX += lookX * yacc / hlook;
                this.motionZ += lookZ * yacc / hlook;
            }

            if (pitchRad < 0) {
                double yacc = hvel * -pitchsin * 0.04;
                this.motionY += yacc * 3.5;
                this.motionX -= lookX * yacc / hlook;
                this.motionZ -= lookZ * yacc / hlook;
            }

            if (hlook > 0) {
                this.motionX += (lookX / hlook * hvel - this.motionX) * 0.1;
                this.motionZ += (lookZ / hlook * hvel - this.motionZ) * 0.1;
            }

            this.motionX *= 0.99;
            this.motionY *= 0.98;
            this.motionZ *= 0.99;

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            ci.cancel();
        }
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;isFlying()Z"), remap = false)
    public boolean stopAiInterference(EntityAmphithere instance) {
        if (amphithereMod$edk()) {
            return false;
        }
        return instance.isFlying();
    }

    @Inject(method = "updateClientControls", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;dismount(Z)V", shift = At.Shift.AFTER), remap = false)
    void captureCustomKey(CallbackInfo ci) {
        //this.amphithereMod$edk(ELYTRA_DIVE_KEY.isKeyDown());
    }

    @Unique
    public void amphithereMod$edk(boolean active) {
        byte state = this.dataManager.get(CONTROL_STATE);
        int bit = 4;
        if (active)
            state = (byte) (state | (1 << bit));
        else
            state = (byte) (state & ~(1 << bit));
        this.dataManager.set(CONTROL_STATE, state);
    }

    @Unique
    public boolean amphithereMod$edk() {
        return (this.dataManager.get(CONTROL_STATE) >> 4 & 1) == 1;
    }
}