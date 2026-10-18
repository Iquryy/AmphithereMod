package amphitheremod.mixin.client;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.IAmphiAnimState;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static amphitheremod.util.AmphiAnimMath.*;
import static amphitheremod.util.AmphiAnimTuning.*;

@Mixin(value = ModelAmphithere.class)
public class AmphithereModel {
    @Shadow(remap = false)
    public AdvancedModelRenderer TailL2, TailR2, WingL, WingR, CrestL1, CrestL2, CrestR2, CrestR1, CrestR3, CrestL3, CrestRB, CrestLB, Neck3, WingR3, WingL3, FingerR4, FingerR3, FingerR2, FingerR1, FingerL4, FingerL3, FingerL2, FingerL1, WingR2, WingL2;
    @Shadow(remap = false)
    public AdvancedModelRenderer BodyUpper, Neck1, Neck2, Tail1, Tail2, Tail3, Tail4, Club;

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 22), remap = false)
    private int ggg(int oldValue) {
        return 21;
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void ddd(CallbackInfo ci) {
        this.TailL2.setRotationPoint(-2.0F, 0.7F, 1.7F);
        this.TailL2.defaultOffsetZ = 0.03735f;
        this.TailL2.defaultOffsetX = 0.01075f;
    }

    @Inject(method = "setRotationAngles", at = @At(value = "TAIL"), order = 5)
    private void rrr(float f, float f1, float f2, float f3, float f4, float f5, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof EntityAmphithere)) return;
        EntityAmphithere amphi = (EntityAmphithere) entity;
        IAmphithereData data = (IAmphithereData) amphi;
        String amphiName = amphi.getName().toLowerCase();
        if (amphiName.isEmpty()) return;
        if (amphiName.equals("shrimp")) {
            this.WingL.isHidden = true;
            this.WingR.isHidden = true;
        } else {
            this.WingL.isHidden = false;
            this.WingR.isHidden = false;
        }
        if (amphiName.equals("bald") && (!amphi.isChild())) {
            this.CrestL1.isHidden = true;
            this.CrestL2.isHidden = true;
            this.CrestR2.isHidden = true;
            this.CrestR1.isHidden = true;
            this.CrestR3.isHidden = true;
            this.CrestL3.isHidden = true;
            this.CrestRB.isHidden = true;
            this.CrestLB.isHidden = true;
        } else {
            this.CrestL1.isHidden = false;
            this.CrestL2.isHidden = false;
            this.CrestR2.isHidden = false;
            this.CrestR1.isHidden = false;
            this.CrestR3.isHidden = false;
            this.CrestL3.isHidden = false;
            this.CrestRB.isHidden = false;
            this.CrestLB.isHidden = false;
        }

        this.Neck3.isHidden = amphiName.equals("h-2");

        if (ConfigHandler.general.maleAndFemale) {
            if (amphi.isChild()) return;
            if (data.amphiMod_master$getGender()) {
                this.TailR2.rotateAngleY -= 0.12f;
                this.TailL2.rotateAngleY += 0.12f;
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------------
    // Flight animation (from the rig sandbox): flap cycle with upstroke fold and body bob, your twist and finger
    // curl timed to the beat, tail reacting to the bob, dive by pitch, smooth turning, no idle tail sway in flight.
    // Runs after Ice and Fire's setRotationAngles, so it first takes out the Ice and Fire motion it replaces.
    // ---------------------------------------------------------------------------------------------------------

    /** Ice and Fire's dive pose targets (ModelAmphithere.setRotationAngles, the diveProgress block). */
    @Unique
    private static final float[][] amphithereMod$DIVE = {
            {0.2617994F, 0F, 0F}, {-0.34906584F, 0F, 0.34906584F}, {0.034906585F, 0F, 0F}, {0.13962634F, 0F, 0F},
            {0F, 0F, 0F}, {0.034906585F, 0F, 0F}, {0.55850536F, 0F, 1.6580628F}, {0.2617994F, 0F, 0F},
            {1.4835298F, 0F, 0F}, {-0.34906584F, 0F, -0.34906584F}, {1.4835298F, 0F, 0F}, {0.06981317F, 0F, 0F},
            {0.13962634F, 0F, 0F}, {0.06981317F, 0F, 0F}, {0.55850536F, 0F, -1.6580628F}};

    @Unique
    private AdvancedModelRenderer[] amphithereMod$diveParts() {
        ModelAmphithere m = (ModelAmphithere) (Object) this;
        return new AdvancedModelRenderer[]{FingerR4, WingL2, FingerR1, FingerL3, m.WingR21, FingerL1, WingR, FingerL4,
                WingR3, WingR2, WingL3, FingerL2, FingerR3, FingerR2, WingL};
    }

    // ---- Switching off the Ice and Fire motion that the flight animation replaces ----
    // Done by intercepting their calls inside setRotationAngles rather than subtracting a copy of their maths
    // afterwards: a copy only cancels if it matches their code exactly (same clock, same numbers), and when it
    // didn't, the leftover grew during each tick and snapped back at the next, which made the wings twitch and the
    // tail shake 20 times a second.

    /** True while setRotationAngles runs for an amphithere that has the new animation state. */
    @Unique
    private boolean amphithereMod$animActive;
    /** How much of Ice and Fire's idle tail sway to keep: all of it on the ground, none in the air. */
    @Unique
    private float amphithereMod$swayKeep = 1F;

    @Inject(method = "setRotationAngles", at = @At("HEAD"))
    private void amphithereMod$beginAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity, CallbackInfo ci) {
        amphithereMod$animActive = entity instanceof EntityAmphithere && entity instanceof IAmphiAnimState;
        amphithereMod$swayKeep = 1F;
        if (amphithereMod$animActive) {
            float pt = MathHelper.clamp(f2 - entity.ticksExisted, 0F, 1F);
            amphithereMod$swayKeep = MathHelper.clamp(((IAmphiAnimState) entity).amphiAnim$groundProgress(pt) / 20F, 0F, 1F);
        }
    }

    /** Ice and Fire's wing flaps (the only flap() calls in setRotationAngles): skipped, the new flap cycle replaces them. */
    @Redirect(method = "setRotationAngles",
            at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/client/model/ModelAmphithere;flap(Lnet/ilexiconn/llibrary/client/model/tools/AdvancedModelRenderer;FFZFFFF)V", remap = false))
    private void amphithereMod$skipBaseFlap(ModelAmphithere self, AdvancedModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        if (!amphithereMod$animActive) self.flap(box, speed, degree, invert, offset, weight, flap, flapAmount);
    }

    /** Ice and Fire's chainSwing calls: the idle tail sway (the one on Tail1..Tail4) fades out in the air; walking is untouched. */
    @Redirect(method = "setRotationAngles",
            at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/client/model/ModelAmphithere;chainSwing([Lnet/ilexiconn/llibrary/client/model/tools/AdvancedModelRenderer;FFDFF)V", remap = false))
    private void amphithereMod$calmTailSway(ModelAmphithere self, AdvancedModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        boolean idleTail = amphithereMod$animActive && boxes.length == 4 && boxes[0] == Tail1;
        self.chainSwing(boxes, speed, degree, rootOffset, swing, idleTail ? swingAmount * amphithereMod$swayKeep : swingAmount);
    }

    @Inject(method = "setRotationAngles", at = @At("TAIL"), order = 10)
    private void amphithereMod$flightAnimation(float f, float f1, float f2, float f3, float f4, float f5, Entity entity, CallbackInfo ci) {
        if (!(entity instanceof EntityAmphithere) || !(entity instanceof IAmphiAnimState)) return;
        EntityAmphithere amphi = (EntityAmphithere) entity;
        IAmphiAnimState st = (IAmphiAnimState) entity;
        float pt = MathHelper.clamp(f2 - amphi.ticksExisted, 0F, 1F); // partial ticks
        // Flap strength and the landing / dive poses, blended between ticks (Ice and Fire steps them once a tick)
        float rawAmt = st.amphiAnim$flapProgress(pt) / 10F;
        float ground = st.amphiAnim$groundProgress(pt);
        boolean buffersOn = amphi.groundProgress <= 0F && amphi.getAnimation() != EntityAmphithere.ANIMATION_WING_BLAST;

        // 1. Ice and Fire's wing flap was skipped and its idle tail sway scaled down in the air, by the redirects
        //    below, so nothing of theirs needs taking back out here.

        // 1b. Ice and Fire posed the landing / take-off from the raw per-tick groundProgress; move it to the blended value.
        float groundDelta = ground - amphi.groundProgress;
        if (groundDelta != 0F) amphithereMod$groundPose((ModelAmphithere) (Object) this, groundDelta);

        // 3. Dive: Ice and Fire's dive blended between ticks, or the dive by pitch, whichever is stronger.
        //    Ice and Fire already posed it with the raw diveProgress, so only the difference is added.
        float extraDive = Math.max(st.amphiAnim$diveProgress(pt), 20F * st.amphiAnim$diveBlend(pt)) - amphi.diveProgress;
        if (extraDive != 0F) {
            AdvancedModelRenderer[] parts = amphithereMod$diveParts();
            for (int i = 0; i < parts.length; i++) amphithereMod$progressRotation(parts[i], extraDive, amphithereMod$DIVE[i]);
        }

        // 4. Flap cycle, faded out past the "flap stops" pitch
        float amt = rawAmt * st.amphiAnim$flapKeep(pt);
        if (amt > 0F) {
            float ph = wrap01(f2 / BEAT_TICKS), lag = 0.05F * TIP_LAG;
            float s1 = stroke(ph), s2 = stroke(ph - lag), s3 = stroke(ph - 2F * lag);
            float fold = fold(ph - lag * 0.5F) * UPSTROKE_FOLD * amt;
            float a = amt * STROKE_SIZE;

            // Arm: main stroke. The rest pose hangs a little low, so the upswing is larger than the downswing.
            float arm = (s1 > 0F ? 1.08F : 0.62F) * a * s1;
            WingL.rotateAngleZ -= arm;
            WingR.rotateAngleZ += arm;
            // Forearm: follows a little later, bends down at the elbow while folded on the upstroke
            float fore = -0.30F * a * s2 + 0.55F * fold;
            WingL2.rotateAngleZ += fore;
            WingR2.rotateAngleZ -= fore;
            WingL2.rotateAngleX += 0.25F * fold;
            WingR2.rotateAngleX += 0.25F * fold;
            // Hand: trails further (wingtip curl at the turnarounds), sweeps back toward the tail when folded
            float hand = -0.22F * a * s3 + 0.25F * fold;
            WingL3.rotateAngleZ += hand;
            WingR3.rotateAngleZ -= hand;
            WingL3.rotateAngleX += 0.85F * fold;
            WingR3.rotateAngleX += 0.85F * fold;
            // Finger bones close their fan when folded and flick slightly at the end of each stroke
            AdvancedModelRenderer[] fl = {FingerL1, FingerL2, FingerL3, FingerL4}, fr = {FingerR1, FingerR2, FingerR3, FingerR4};
            for (int i = 0; i < 4; i++) {
                float close = 0.85F * fold, flick = 0.10F * a * s3 * (0.6F + 0.15F * i);
                fl[i].rotateAngleX -= fl[i].defaultRotationX * close;
                fr[i].rotateAngleX -= fr[i].defaultRotationX * close;
                fl[i].rotateAngleZ -= flick;
                fr[i].rotateAngleZ += flick;
            }

            // Body bob: lifted by the downstroke, sinks on the upstroke; neck counters it to steady the head
            float hb = stroke(ph - 0.08F);
            BodyUpper.rotationPointY += BODY_BOB_PX * amt * hb;
            BodyUpper.rotateAngleX += 0.035F * amt * hb;
            float bobK = BODY_BOB_PX / 2.2F;
            Neck1.rotateAngleX -= 0.06F * amt * bobK * hb;
            Neck2.rotateAngleX += 0.025F * amt * bobK * hb;

            // Your twist and finger curl, timed to the same beat with the offsets you tuned
            amphithereMod$syncedPieces(ph, amt);
        }

        // 5. Tail: reacts to the body bob (up/down) and to turning (sideways), both springy
        AdvancedModelRenderer[] tail = {Tail1, Tail2, Tail3, Tail4, Club};
        for (int i = 0; i < tail.length; i++) {
            tail[i].rotateAngleX += st.amphiAnim$bobTail(i, pt) * ((float) Math.PI / 180F);
            tail[i].rotateAngleY += st.amphiAnim$turnTail(i, pt) * ((float) Math.PI / 180F);
        }

        // 6. Turning: bank into the turn (in the air) and let the neck lead toward where it's facing
        if (buffersOn) BodyUpper.rotateAngleZ += st.amphiAnim$bankRoll(pt) * ((float) Math.PI / 180F);
        float lead = st.amphiAnim$neckLead(pt) * ((float) Math.PI / 180F);
        Neck1.rotateAngleY += lead * 0.3F;
        Neck2.rotateAngleY += lead * 0.35F;
        Neck3.rotateAngleY += lead * 0.35F;
    }

    /**
     * Your WING_TWIST / FINGER_CURL pieces. They were written against cos(ticks * 0.2 + offset), where +1 means
     * wings down; here that wave is the beat's own stroke (also +1 = down), and each offset in radians becomes the
     * same share of a beat, so they stay where you tuned them relative to the wing.
     */
    @Unique
    private void amphithereMod$syncedPieces(float ph, float amt) {
        float df = 0.5F * amt, fc = df * 0.75F * FINGER_CURL_STRENGTH;
        amphithereMod$swing(WingL, ph, df * 0.45F * WING_TWIST_STRENGTH, true, 0F, 0.15F);
        amphithereMod$swing(WingR, ph, df * 0.45F * WING_TWIST_STRENGTH, false, 0F, 0.15F);
        amphithereMod$swing(WingL2, ph, df * 0.40F * WING_TWIST_STRENGTH, true, -0.25F, 0.25F);
        amphithereMod$swing(WingR2, ph, df * 0.40F * WING_TWIST_STRENGTH, false, -0.25F, 0.25F);
        amphithereMod$swing(WingL3, ph, df * 0.35F * WING_TWIST_STRENGTH, true, -0.5F, 0.35F);
        amphithereMod$swing(WingR3, ph, df * 0.35F * WING_TWIST_STRENGTH, false, -0.5F, 0.35F);
        AdvancedModelRenderer[] fl = {FingerL1, FingerL2, FingerL3, FingerL4}, fr = {FingerR1, FingerR2, FingerR3, FingerR4};
        float[] w = {0.3F, 0.4F, 0.5F, 0.6F};
        for (int i = 0; i < 4; i++) {
            fl[i].rotateAngleZ += amphithereMod$wave(ph, -1.5F) * fc * w[i];
            fr[i].rotateAngleZ -= amphithereMod$wave(ph, -1.5F) * fc * w[i];
        }
    }

    @Unique
    private static float amphithereMod$wave(float ph, float offsetRadians) {
        return -stroke(ph + offsetRadians / (2F * PI));
    }

    @Unique
    private static void amphithereMod$swing(AdvancedModelRenderer box, float ph, float degree, boolean invert, float offset, float weight) {
        float m = amphithereMod$wave(ph, offset) * degree;
        box.rotateAngleY += (invert ? -m : m) * weight;
    }

    /** Ice and Fire's landing pose (the groundProgress block of ModelAmphithere.setRotationAngles), applied for a given progress. */
    @Unique
    private static void amphithereMod$groundPose(ModelAmphithere m, float p) {
        final float PI10 = (float) Math.PI / 10F, PI4 = (float) Math.PI / 4F;
        amphithereMod$progressRotation(m.Tail1, p, new float[]{-0.045553092F, 0F, 0F});
        amphithereMod$progressRotation(m.CrestR2, p, new float[]{1.7453293F, -0.61086524F, -PI10});
        amphithereMod$progressRotation(m.FingerR4, p, new float[]{-0.2617994F, 0F, 0F});
        amphithereMod$progressRotation(m.TailL1, p, new float[]{1.3962634F, -0.06981317F, 0F});
        amphithereMod$progressRotation(m.Neck2, p, new float[]{-0.4553564F, 0F, 0F});
        amphithereMod$progressRotation(m.FingerR3, p, new float[]{-0.13962634F, 0F, 0F});
        amphithereMod$progressRotation(m.FingerL1, p, new float[]{0.13962634F, 0F, 0F});
        amphithereMod$progressRotation(m.WingR, p, new float[]{0.08726646F, 0F, 1.134464F});
        amphithereMod$progressRotation(m.FingerL4, p, new float[]{-0.2617994F, 0F, 0F});
        amphithereMod$progressRotation(m.Teeth2, p, new float[]{0F, 0F, 0F});
        amphithereMod$progressRotation(m.FingerR2, p, new float[]{0F, 0F, 0F});
        amphithereMod$progressRotation(m.Beak, p, new float[]{0.6981317F, 0F, 0F});
        amphithereMod$progressRotation(m.BodyUpper, p, new float[]{0F, 0F, 0F});
        amphithereMod$progressRotation(m.Club, p, new float[]{0.18203785F, 0F, 0F});
        amphithereMod$progressRotation(m.CrestLB, p, new float[]{1.7453293F, 0.27314404F, 0F});
        amphithereMod$progressRotation(m.FingerR1, p, new float[]{0.13962634F, 0F, 0F});
        amphithereMod$progressRotation(m.HeadFront, p, new float[]{-0.22759093F, 0F, 0F});
        amphithereMod$progressRotation(m.Neck1, p, new float[]{-0.18203785F, 0F, -0.045553092F});
        amphithereMod$progressRotation(m.FingerL2, p, new float[]{0F, 0F, 0F});
        amphithereMod$progressRotation(m.WingR3, p, new float[]{0.12217305F, -0.17453292F, -0.12217305F});
        amphithereMod$progressRotation(m.Teeth1, p, new float[]{0F, 0F, 0F});
        amphithereMod$progressRotation(m.Tail4, p, new float[]{-0.045553092F, 0F, 0F});
        amphithereMod$progressRotation(m.WingL3, p, new float[]{0.12217305F, 0.17453292F, 0.12217305F});
        amphithereMod$progressRotation(m.Jaw, p, new float[]{-0.31869712F, 0F, 0F});
        amphithereMod$progressRotation(m.Tail2, p, new float[]{0.091106184F, 0F, 0F});
        amphithereMod$progressRotation(m.CrestRB, p, new float[]{1.7453293F, -0.27314404F, 0F});
        amphithereMod$progressRotation(m.BodyLower, p, new float[]{-0.13665928F, 0F, 0F});
        amphithereMod$progressRotation(m.CrestR3, p, new float[]{1.7453293F, -0.87266463F, -0.7285004F});
        amphithereMod$progressRotation(m.WingL21, p, new float[]{0F, 0F, 0F});
        amphithereMod$progressRotation(m.Neck3, p, new float[]{0.18203785F, 0F, 0F});
        amphithereMod$progressRotation(m.TailL2, p, new float[]{1.3962634F, 0.31869712F, 0F});
        amphithereMod$progressRotation(m.FingerL3, p, new float[]{-0.13962634F, 0F, 0F});
        amphithereMod$progressRotation(m.CrestL3, p, new float[]{1.7453293F, 0.87266463F, PI4});
        amphithereMod$progressRotation(m.CrestL2, p, new float[]{1.7453293F, 0.61086524F, PI10});
        amphithereMod$progressRotation(m.WingR21, p, new float[]{0F, 0F, 0F});
        amphithereMod$progressRotation(m.TailR1, p, new float[]{1.3962634F, 0.06981317F, 0F});
        amphithereMod$progressRotation(m.Head, p, new float[]{0.8196066F, 0F, 0F});
        amphithereMod$progressRotation(m.WingR2, p, new float[]{1.4835298F, 0F, 0.17453292F});
        amphithereMod$progressRotation(m.WingL, p, new float[]{0.08726646F, 0F, -1.134464F});
        amphithereMod$progressRotation(m.CrestR1, p, new float[]{1.7453293F, -0.08726646F, 0F});
        amphithereMod$progressRotation(m.WingL2, p, new float[]{1.4835298F, 0F, -0.17453292F});
        amphithereMod$progressRotation(m.Tail3, p, new float[]{0.045553092F, 0F, 0F});
        amphithereMod$progressRotation(m.TailR2, p, new float[]{1.3962634F, -0.31869712F, 0F});
        amphithereMod$progressRotation(m.CrestL1, p, new float[]{1.7453293F, 0.08726646F, 0F});
        m.BodyUpper.rotationPointY += p * (18.0F - m.BodyUpper.defaultPositionY) / 20.0F; // progressPosition(BodyUpper, 0, 18, 0)
    }

    /** Same as Ice and Fire's ModelDragonBase.progressRotation. */
    @Unique
    private static void amphithereMod$progressRotation(AdvancedModelRenderer box, float progress, float[] to) {
        box.rotateAngleX += progress * (to[0] - box.defaultRotationX) / 20F;
        box.rotateAngleY += progress * (to[1] - box.defaultRotationY) / 20F;
        box.rotateAngleZ += progress * (to[2] - box.defaultRotationZ) / 20F;
    }
}