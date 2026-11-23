package amphitheremod.mixin;

import amphitheremod.util.AmphiAnimMath;
import amphitheremod.util.IAmphiAnimState;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Client-side flight animation state, ticked once per tick and read by the model mixin (client.mixin.AmphiModelMixin)
 * with partial-tick interpolation. Port of 1.12.2's AmphiAnimState: dive blend from pitch, flap fade in steep dives,
 * body bob from the wing stroke (and the tail and rider following it), bank roll and tail swing in turns, the neck
 * leading the body, and a smoothed body pitch. Only does anything on the client.
 */
@Mixin(EntityAmphithere.class)
public abstract class AmphiAnimStateMixin implements IAmphiAnimState {
    @Unique
    private float amphiAnim$smPitch;
    @Unique
    private float amphiAnim$diveW;
    @Unique
    private float amphiAnim$prevDiveW;
    @Unique
    private float amphiAnim$flapK = 1.0F;
    @Unique
    private float amphiAnim$prevFlapK = 1.0F;
    @Unique
    private boolean amphiAnim$yawReady;
    @Unique
    private float amphiAnim$bodyYaw;
    @Unique
    private float amphiAnim$prevBodyYaw;
    @Unique
    private float amphiAnim$roll;
    @Unique
    private float amphiAnim$prevRoll;
    @Unique
    private float amphiAnim$turnRate;
    @Unique
    private final float[] amphiAnim$yawHist = new float[24];
    @Unique
    private final float[] amphiAnim$turnA = new float[5];
    @Unique
    private final float[] amphiAnim$turnPrev = new float[5];
    @Unique
    private final float[] amphiAnim$turnV = new float[5];
    @Unique
    private float amphiAnim$flapP;
    @Unique
    private float amphiAnim$prevFlapP;
    @Unique
    private float amphiAnim$diveP;
    @Unique
    private float amphiAnim$prevDiveP;
    @Unique
    private float amphiAnim$groundP;
    @Unique
    private float amphiAnim$prevGroundP;
    @Unique
    private boolean amphiAnim$progressReady;
    @Unique
    private float amphiAnim$prevBob;
    @Unique
    private final float[] amphiAnim$bobHist = new float[16];
    @Unique
    private final float[] amphiAnim$bobA = new float[5];
    @Unique
    private final float[] amphiAnim$bobPrev = new float[5];
    @Unique
    private final float[] amphiAnim$bobV = new float[5];
    @Unique
    private float amphiAnim$bodyPitch;
    @Unique
    private float amphiAnim$prevBodyPitch;

    @Inject(method = "tick", at = @At("TAIL"))
    private void amphiAnim$tick(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!amphi.level().isClientSide) {
            return;
        }
        // 1 in the air, 0 on the ground
        float air = 1.0F - amphi.groundProgress / 20.0F;
        if (!this.amphiAnim$progressReady) {
            this.amphiAnim$flapP = amphi.flapProgress;
            this.amphiAnim$diveP = amphi.diveProgress;
            this.amphiAnim$groundP = amphi.groundProgress;
            this.amphiAnim$progressReady = true;
        }
        // Keep last tick's progress values so the model can interpolate them (Ice and Fire's jump a step per tick)
        this.amphiAnim$prevFlapP = this.amphiAnim$flapP;
        this.amphiAnim$flapP = amphi.flapProgress;
        this.amphiAnim$prevDiveP = this.amphiAnim$diveP;
        this.amphiAnim$diveP = amphi.diveProgress;
        this.amphiAnim$prevGroundP = this.amphiAnim$groundP;
        this.amphiAnim$groundP = amphi.groundProgress;

        // Dive pose and flap fade both follow a smoothed pitch
        this.amphiAnim$smPitch += (amphi.getXRot() - this.amphiAnim$smPitch) * 0.18F;
        this.amphiAnim$prevDiveW = this.amphiAnim$diveW;
        this.amphiAnim$prevFlapK = this.amphiAnim$flapK;
        float wantDive = AmphiAnimMath.smooth01(17.0F, 50.0F, this.amphiAnim$smPitch) * air;
        float wantFlap = 1.0F - AmphiAnimMath.smooth01(23.0F, 35.0F, this.amphiAnim$smPitch) * air;
        this.amphiAnim$diveW = AmphiAnimMath.stepToward(this.amphiAnim$diveW, wantDive, 1.0F / 14.0F);
        this.amphiAnim$flapK = AmphiAnimMath.stepToward(this.amphiAnim$flapK, wantFlap, 0.1F);

        // Body bob from the wing stroke; the tail follows it a little later, segment by segment
        float amt = amphi.flapProgress / 10.0F * this.amphiAnim$flapK;
        float bob = 2.2F * amt * AmphiAnimMath.stroke(amphi.tickCount / AmphiAnimMath.BEAT_TICKS - 0.08F);
        push(this.amphiAnim$bobHist, bob - this.amphiAnim$prevBob);
        this.amphiAnim$prevBob = bob;
        for (int i = 0; i < 5; i++) {
            int k = Math.min(15, Math.round(i * 3.0F));
            float want = Mth.clamp(this.amphiAnim$bobHist[k] * 8.0F * (0.4F + 0.2F * i) * 0.8F, -20.0F, 20.0F);
            spring(this.amphiAnim$bobA, this.amphiAnim$bobPrev, this.amphiAnim$bobV, i, want, 0.18F, 0.3F, 3.0F);
        }

        // Body yaw: turns instantly with a rider, eases toward the facing otherwise; the turn rate drives bank and tail
        if (!this.amphiAnim$yawReady) {
            this.amphiAnim$bodyYaw = this.amphiAnim$prevBodyYaw = amphi.getYRot();
            this.amphiAnim$yawReady = true;
        }
        float before = this.amphiAnim$bodyYaw;
        float diff = Mth.wrapDegrees(amphi.getYRot() - this.amphiAnim$bodyYaw);
        boolean riderSteers = amphi.getControllingPassenger() instanceof Player;
        this.amphiAnim$bodyYaw += riderSteers ? diff : Mth.clamp(diff * 0.35F, -30.0F, 30.0F);
        float vel = this.amphiAnim$bodyYaw - before;
        this.amphiAnim$bodyYaw = amphi.getYRot() - Mth.wrapDegrees(amphi.getYRot() - this.amphiAnim$bodyYaw);
        this.amphiAnim$prevBodyYaw = this.amphiAnim$bodyYaw - vel;
        amphi.yBodyRot = this.amphiAnim$bodyYaw;
        this.amphiAnim$turnRate += (vel - this.amphiAnim$turnRate) * 0.3F;
        float rate = this.amphiAnim$turnRate * AmphiAnimMath.smooth01(0.6F, 1.8F, Math.abs(this.amphiAnim$turnRate));
        push(this.amphiAnim$yawHist, rate);
        this.amphiAnim$prevRoll = this.amphiAnim$roll;
        float wantRoll = Mth.clamp(-rate * 4.0F, -60.0F, 60.0F) * air;
        this.amphiAnim$roll += (wantRoll - this.amphiAnim$roll) * 0.25F;
        for (int i = 0; i < 5; i++) {
            float want = Mth.clamp(-this.amphiAnim$yawHist[0] * 1.35F * (0.35F + 0.2F * i), -35.0F, 35.0F);
            spring(this.amphiAnim$turnA, this.amphiAnim$turnPrev, this.amphiAnim$turnV, i, want, 0.2F, 0.36F, 4.0F);
        }

        // Smoothed body pitch (1.12.2's pitch buffer): eases toward the pitch, at most 5 degrees a tick, and
        // relaxes back to level while gliding
        this.amphiAnim$prevBodyPitch = this.amphiAnim$bodyPitch;
        if (!amphi.onGround()) {
            float want;
            if (amphi.diveProgress == 0.0F && amphi.flapProgress == 0.0F && amphi.isFlying()) {
                want = this.amphiAnim$bodyPitch * 0.95F;
            } else {
                float target = Mth.clamp(amphi.getXRot(), -50.0F, 50.0F);
                want = this.amphiAnim$bodyPitch + (target - this.amphiAnim$bodyPitch) * 0.25F;
            }
            this.amphiAnim$bodyPitch += Mth.clamp(want - this.amphiAnim$bodyPitch, -5.0F, 5.0F);
        }
    }

    @Unique
    private static void push(float[] hist, float value) {
        System.arraycopy(hist, 0, hist, 1, hist.length - 1);
        hist[0] = value;
    }

    /** Damped spring step for segment i toward want. */
    @Unique
    private static void spring(float[] a, float[] prev, float[] v, int i, float want, float stiff, float damp, float maxSpeed) {
        prev[i] = a[i];
        v[i] = Mth.clamp(v[i] * (1.0F - damp) + (want - a[i]) * stiff, -maxSpeed, maxSpeed);
        a[i] += v[i];
    }

    // The rider bobs with the body
    @Inject(method = "positionRider", at = @At("TAIL"))
    private void amphiAnim$bobRider(Entity passenger, Entity.MoveFunction callback, CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!amphi.level().isClientSide || !amphi.hasPassenger(passenger)) {
            return;
        }
        float bob = amphiAnim$bobBlocks();
        if (bob != 0.0F) {
            passenger.setPos(passenger.getX(), passenger.getY() - bob, passenger.getZ());
        }
    }

    @Override
    public float amphiAnim$diveBlend(float pt) {
        return AmphiAnimMath.lerp(this.amphiAnim$prevDiveW, this.amphiAnim$diveW, pt);
    }

    @Override
    public float amphiAnim$flapKeep(float pt) {
        return AmphiAnimMath.lerp(this.amphiAnim$prevFlapK, this.amphiAnim$flapK, pt);
    }

    @Override
    public float amphiAnim$bankRoll(float pt) {
        return AmphiAnimMath.lerp(this.amphiAnim$prevRoll, this.amphiAnim$roll, pt);
    }

    @Override
    public float amphiAnim$neckLead(float pt) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        float facing = AmphiAnimMath.lerp(amphi.yRotO, amphi.getYRot(), pt);
        float body = AmphiAnimMath.lerp(this.amphiAnim$prevBodyYaw, this.amphiAnim$bodyYaw, pt);
        return Mth.clamp(Mth.wrapDegrees(facing - body), -90.0F, 90.0F);
    }

    @Override
    public float amphiAnim$turnTail(int i, float pt) {
        return AmphiAnimMath.lerp(this.amphiAnim$turnPrev[i], this.amphiAnim$turnA[i], pt);
    }

    @Override
    public float amphiAnim$bobTail(int i, float pt) {
        return AmphiAnimMath.lerp(this.amphiAnim$bobPrev[i], this.amphiAnim$bobA[i], pt);
    }

    @Override
    public float amphiAnim$flapProgress(float pt) {
        return this.amphiAnim$progressReady ? AmphiAnimMath.lerp(this.amphiAnim$prevFlapP, this.amphiAnim$flapP, pt) : ((EntityAmphithere) (Object) this).flapProgress;
    }

    @Override
    public float amphiAnim$diveProgress(float pt) {
        return this.amphiAnim$progressReady ? AmphiAnimMath.lerp(this.amphiAnim$prevDiveP, this.amphiAnim$diveP, pt) : ((EntityAmphithere) (Object) this).diveProgress;
    }

    @Override
    public float amphiAnim$groundProgress(float pt) {
        return this.amphiAnim$progressReady ? AmphiAnimMath.lerp(this.amphiAnim$prevGroundP, this.amphiAnim$groundP, pt) : ((EntityAmphithere) (Object) this).groundProgress;
    }

    @Override
    public float amphiAnim$bodyPitch(float pt) {
        return AmphiAnimMath.lerp(this.amphiAnim$prevBodyPitch, this.amphiAnim$bodyPitch, pt);
    }

    @Override
    public float amphiAnim$bobBlocks() {
        // Model pixels to blocks at the renderer's 2x scale
        return this.amphiAnim$prevBob * 0.125F;
    }
}
