package amphitheremod.mixin.client;

import amphitheremod.util.AmphiAnimTuning;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IFChainBuffer.class)
public class ChainBuffer {

    @Shadow(remap = false)
    private float pitchVariation;
    @Shadow(remap = false)
    private float prevPitchVariation;
    @Shadow(remap = false)
    private float yawVariation;
    @Shadow(remap = false)
    private float prevYawVariation;

    // Replaces Ice and Fire's pitch buffer for every amphithere, ridden or not. The original adds
    // (prevPitch - pitch), the opposite sign of the pitch itself, so a nose-down dive (for example after the
    // flute, which pushes pitch to 75) tilted the body nose-up. This eases the body toward the real pitch instead,
    // and never tilts it more than BODY_TILT_MAX_DEG_PER_TICK per tick, so hard mouse flicks don't jerk the body.
    @Inject(method = "calculateChainWaveBuffer(FIFFLnet/minecraft/entity/EntityLivingBase;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void smoothAmphitherePitch(float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLivingBase entity, CallbackInfo ci) {
        if (entity instanceof EntityAmphithere) {
            EntityAmphithere amphi = (EntityAmphithere) entity;
            float smoothSpeed = 0.25f;
            // The renderer blends from prevPitchVariation to pitchVariation within each tick, so prev must be last
            // tick's value. Easing the two towards separate targets made the tilt jitter between frames.
            this.prevPitchVariation = this.pitchVariation;
            float want;
            if (amphi.diveProgress == 0 && amphi.flapProgress == 0 && amphi.isFlying()) {
                want = this.pitchVariation * 0.95f;
            } else {
                float targetPitch = MathHelper.clamp(entity.rotationPitch, -50f, 50f);
                want = this.pitchVariation + (targetPitch - this.pitchVariation) * smoothSpeed;
            }
            float maxStep = AmphiAnimTuning.BODY_TILT_MAX_DEG_PER_TICK;
            this.pitchVariation += MathHelper.clamp(want - this.pitchVariation, -maxStep, maxStep);
            ci.cancel();
        }
    }

    // Ice and Fire's roll buffer: snaps the body into a roll from yaw changes and drops it 10 degrees a tick.
    // Amphitheres get a smooth bank from AmphiAnimState instead, so keep this at zero.
    // Its "flap the wings on a sharp turn" side effect is kept.
    @Inject(method = "calculateChainFlapBuffer(FIFFLnet/minecraft/entity/EntityLivingBase;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void noAmphithereRollBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLivingBase entity, CallbackInfo ci) {
        if (entity instanceof EntityAmphithere) {
            this.yawVariation = 0F;
            this.prevYawVariation = 0F;
            if (Math.abs(entity.prevRenderYawOffset - entity.renderYawOffset) > 15D) ((EntityAmphithere) entity).flapWings();
            ci.cancel();
        }
    }

    // Ice and Fire's tail buffer: swings the tail from yaw changes, and settles at +5 degrees instead of 0 after
    // every turn, leaving the tail bent. Amphitheres get the springy turning tail from AmphiAnimState instead.
    @Inject(method = "calculateChainSwingBuffer(FIFFLnet/minecraft/entity/EntityLivingBase;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void noAmphithereTailBuffer(float maxAngle, int bufferTime, float angleDecrement, float divisor, EntityLivingBase entity, CallbackInfo ci) {
        if (entity instanceof EntityAmphithere) {
            this.yawVariation = 0F;
            this.prevYawVariation = 0F;
            ci.cancel();
        }
    }
}