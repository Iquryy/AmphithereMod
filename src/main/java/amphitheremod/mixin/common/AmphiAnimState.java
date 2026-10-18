package amphitheremod.mixin.common;

import amphitheremod.util.IAmphiAnimState;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static amphitheremod.util.AmphiAnimMath.*;
import static amphitheremod.util.AmphiAnimTuning.*;

/**
 * Animation state that has to remember things between ticks: eased pitch, the dive blend and flap fade,
 * smoothed body yaw and bank, and the springy tail. Ticked on the client only; the server never uses it.
 */
@Mixin(EntityAmphithere.class)
public abstract class AmphiAnimState implements IAmphiAnimState {

    // dive by pitch
    @Unique private float amphiAnim$smPitch, amphiAnim$diveW, amphiAnim$prevDiveW;
    @Unique private float amphiAnim$flapK = 1F, amphiAnim$prevFlapK = 1F;
    // turning
    @Unique private boolean amphiAnim$yawReady;
    @Unique private float amphiAnim$bodyYaw, amphiAnim$prevBodyYaw, amphiAnim$roll, amphiAnim$prevRoll, amphiAnim$turnRate;
    @Unique private final float[] amphiAnim$yawHist = new float[YAW_HIST];
    @Unique private final float[] amphiAnim$turnA = new float[TAIL_SEGMENTS], amphiAnim$turnPrev = new float[TAIL_SEGMENTS], amphiAnim$turnV = new float[TAIL_SEGMENTS];
    // Ice and Fire's per-tick progress values, last tick and this tick, for blending between frames
    @Unique private float amphiAnim$flapP, amphiAnim$prevFlapP, amphiAnim$diveP, amphiAnim$prevDiveP, amphiAnim$groundP, amphiAnim$prevGroundP;
    @Unique private boolean amphiAnim$progressReady;
    // tail vs body bob
    @Unique private float amphiAnim$prevBob;
    @Unique private final float[] amphiAnim$bobHist = new float[BOB_HIST];
    @Unique private final float[] amphiAnim$bobA = new float[TAIL_SEGMENTS], amphiAnim$bobPrev = new float[TAIL_SEGMENTS], amphiAnim$bobV = new float[TAIL_SEGMENTS];

    @Inject(method = "onUpdate", at = @At("TAIL"))
    private void amphiAnim$tick(CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!amphi.world.isRemote) return;
        float air = 1F - amphi.groundProgress / 20F;

        // Snapshot Ice and Fire's progress values (already updated this tick) so the model can blend them
        if (!amphiAnim$progressReady) {
            amphiAnim$flapP = amphi.flapProgress; amphiAnim$diveP = amphi.diveProgress; amphiAnim$groundP = amphi.groundProgress;
            amphiAnim$progressReady = true;
        }
        amphiAnim$prevFlapP = amphiAnim$flapP; amphiAnim$flapP = amphi.flapProgress;
        amphiAnim$prevDiveP = amphiAnim$diveP; amphiAnim$diveP = amphi.diveProgress;
        amphiAnim$prevGroundP = amphiAnim$groundP; amphiAnim$groundP = amphi.groundProgress;

        // ---- Dive by pitch: ease the pitch, then move the dive blend and flap fade at a limited speed ----
        amphiAnim$smPitch += (amphi.rotationPitch - amphiAnim$smPitch) * PITCH_EASE;
        amphiAnim$prevDiveW = amphiAnim$diveW;
        amphiAnim$prevFlapK = amphiAnim$flapK;
        float wantDive = smooth01(DIVE_STARTS_DEG, FULL_DIVE_DEG, amphiAnim$smPitch) * air;
        float wantFlap = 1F - smooth01(FLAP_STOPS_DEG - FLAP_FADE_DEG, FLAP_STOPS_DEG + FLAP_FADE_DEG, amphiAnim$smPitch) * air;
        amphiAnim$diveW = stepToward(amphiAnim$diveW, wantDive, DIVE_RATE);
        amphiAnim$flapK = stepToward(amphiAnim$flapK, wantFlap, FLAP_RATE);

        // ---- Tail reacting to the body bob ----
        float amt = amphi.flapProgress / 10F * amphiAnim$flapK;
        float bob = BODY_BOB_PX * amt * stroke(amphi.ticksExisted / BEAT_TICKS - 0.08F); // + = lower (model y points down)
        amphiAnim$push(amphiAnim$bobHist, bob - amphiAnim$prevBob);
        amphiAnim$prevBob = bob;
        float dir = TAIL_BOB_REVERSED ? -1F : 1F;
        for (int i = 0; i < TAIL_SEGMENTS; i++) {
            int k = Math.min(BOB_HIST - 1, Math.round(i * TAIL_BOB_DELAY));
            // body moving up (negative) -> tail tips down (negative rotateAngleX), and the reverse
            float want = MathHelper.clamp(dir * amphiAnim$bobHist[k] * 8F * (0.4F + 0.2F * i) * TAIL_FOLLOWS_BOB, -BOB_TAIL_SEG_MAX, BOB_TAIL_SEG_MAX);
            amphiAnim$spring(amphiAnim$bobA, amphiAnim$bobPrev, amphiAnim$bobV, i, want, BOB_TAIL_STIFF, BOB_TAIL_DAMP, BOB_TAIL_SPEED_MAX);
        }

        // ---- Turning: the body eases toward rotationYaw (the rider's yaw when ridden) at a limited speed ----
        if (!amphiAnim$yawReady) {
            amphiAnim$bodyYaw = amphiAnim$prevBodyYaw = amphi.rotationYaw;
            amphiAnim$yawReady = true;
        }
        float before = amphiAnim$bodyYaw;
        float diff = MathHelper.wrapDegrees(amphi.rotationYaw - amphiAnim$bodyYaw);
        boolean riderSteers = amphi.getControllingPassenger() instanceof EntityPlayer;
        if (riderSteers && RIDER_TURNS_INSTANTLY) {
            // Ridden: the body follows the rider's turning straight away. Bank, neck and tail still react to it.
            amphiAnim$bodyYaw += diff;
        } else {
            amphiAnim$bodyYaw += MathHelper.clamp(diff * TURN_EASE, -TURN_MAX_DEG_PER_TICK, TURN_MAX_DEG_PER_TICK);
        }
        float vel = amphiAnim$bodyYaw - before;
        // keep the value within 180 of rotationYaw (same angle), so the renderer's own wrapping never sees a jump
        amphiAnim$bodyYaw = amphi.rotationYaw - MathHelper.wrapDegrees(amphi.rotationYaw - amphiAnim$bodyYaw);
        amphiAnim$prevBodyYaw = amphiAnim$bodyYaw - vel;
        amphi.renderYawOffset = amphiAnim$bodyYaw;

        // Bank and tail are driven by a smoothed turn rate, not the raw per-tick yaw change. Mouse movement arrives
        // unevenly from tick to tick even when steering smoothly; with the body following the rider instantly that
        // jitter went straight into the bank (rocking body and wings) and the tail springs (shaking). Tiny rates
        // are faded out entirely so small corrections don't wobble anything.
        amphiAnim$turnRate += (vel - amphiAnim$turnRate) * TURN_RATE_SMOOTHING;
        float rate = amphiAnim$turnRate * smooth01(TURN_DEADZONE_DEG, TURN_DEADZONE_DEG * 3F, Math.abs(amphiAnim$turnRate));
        amphiAnim$push(amphiAnim$yawHist, rate);
        amphiAnim$prevRoll = amphiAnim$roll;
        float wantRoll = MathHelper.clamp(-rate * BANK, -BANK_MAX_DEG, BANK_MAX_DEG) * air;
        amphiAnim$roll += (wantRoll - amphiAnim$roll) * BANK_EASE;
        for (int i = 0; i < TAIL_SEGMENTS; i++) {
            int k = Math.min(YAW_HIST - 1, Math.round(i * TAIL_TURN_DELAY));
            float want = MathHelper.clamp(-amphiAnim$yawHist[k] * TAIL_FOLLOWS_TURN * (0.35F + 0.2F * i), -TURN_TAIL_SEG_MAX, TURN_TAIL_SEG_MAX);
            amphiAnim$spring(amphiAnim$turnA, amphiAnim$turnPrev, amphiAnim$turnV, i, want, TURN_TAIL_STIFF, TURN_TAIL_DAMP, TURN_TAIL_SPEED_MAX);
        }
    }

    @Unique
    private static void amphiAnim$push(float[] hist, float value) {
        System.arraycopy(hist, 0, hist, 1, hist.length - 1);
        hist[0] = value;
    }

    /** One tick of a damped spring with a speed cap: momentum, settling, and no snapping between ticks. */
    @Unique
    private static void amphiAnim$spring(float[] a, float[] prev, float[] v, int i, float want, float stiff, float damp, float maxSpeed) {
        prev[i] = a[i];
        v[i] = MathHelper.clamp(v[i] * (1F - damp) + (want - a[i]) * stiff, -maxSpeed, maxSpeed);
        a[i] += v[i];
    }

    @Override
    public float amphiAnim$diveBlend(float pt) { return lerp(amphiAnim$prevDiveW, amphiAnim$diveW, pt); }

    @Override
    public float amphiAnim$flapKeep(float pt) { return lerp(amphiAnim$prevFlapK, amphiAnim$flapK, pt); }

    @Override
    public float amphiAnim$bankRoll(float pt) { return lerp(amphiAnim$prevRoll, amphiAnim$roll, pt); }

    @Override
    public float amphiAnim$neckLead(float pt) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        float facing = lerp(amphi.prevRotationYaw, amphi.rotationYaw, pt);
        float body = lerp(amphiAnim$prevBodyYaw, amphiAnim$bodyYaw, pt);
        return MathHelper.clamp(MathHelper.wrapDegrees(facing - body), -NECK_LEAD_MAX_DEG, NECK_LEAD_MAX_DEG) * NECK_LEADS;
    }

    @Override
    public float amphiAnim$turnTail(int i, float pt) { return lerp(amphiAnim$turnPrev[i], amphiAnim$turnA[i], pt); }

    @Override
    public float amphiAnim$bobTail(int i, float pt) { return lerp(amphiAnim$bobPrev[i], amphiAnim$bobA[i], pt); }

    @Override
    public float amphiAnim$flapProgress(float pt) { return amphiAnim$progressReady ? lerp(amphiAnim$prevFlapP, amphiAnim$flapP, pt) : ((EntityAmphithere) (Object) this).flapProgress; }

    @Override
    public float amphiAnim$diveProgress(float pt) { return amphiAnim$progressReady ? lerp(amphiAnim$prevDiveP, amphiAnim$diveP, pt) : ((EntityAmphithere) (Object) this).diveProgress; }

    @Override
    public float amphiAnim$groundProgress(float pt) { return amphiAnim$progressReady ? lerp(amphiAnim$prevGroundP, amphiAnim$groundP, pt) : ((EntityAmphithere) (Object) this).groundProgress; }

    @Override
    public float amphiAnim$bobBlocks() { return amphiAnim$prevBob * MODEL_PX_TO_BLOCKS; }

    // The rider bobs with the body. Ice and Fire places the rider once per tick in updatePassenger; this moves
    // them by this tick's bob, and the renderer blends the rider's position between ticks as usual.
    // Client only: it's a visual offset and the server's position for the rider stays as Ice and Fire sets it.
    @Inject(method = "updatePassenger", at = @At("TAIL"))
    private void amphiAnim$bobRider(net.minecraft.entity.Entity passenger, CallbackInfo ci) {
        EntityAmphithere amphi = (EntityAmphithere) (Object) this;
        if (!amphi.world.isRemote || !amphi.isPassenger(passenger)) return;
        float bob = amphiAnim$bobBlocks();
        if (bob != 0F) passenger.setPosition(passenger.posX, passenger.posY - bob, passenger.posZ);
    }
}