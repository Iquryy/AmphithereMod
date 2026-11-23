package amphitheremod.mixin.client;

import amphitheremod.config.AmphiConfig;
import amphitheremod.util.AmphiAnimMath;
import amphitheremod.util.IAmphiAnimState;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Model changes from 1.12.2: the left tail fin lined up with the right one, name easter eggs ("Shrimp" has no wings,
 * "Bald" no crest, "H-2" no head), female tail fins spread a little, and the reworked flight animation (wing beat with
 * fold and finger flick, body bob, dive pose blended from pitch, bank in turns, tail following the bob and the turn,
 * neck leading turns, smoothed body pitch, and ground pose eased with partial ticks).
 */
@Mixin(value = ModelAmphithere.class, remap = false)
public abstract class AmphiModelMixin {
    // setupAnim overrides a Minecraft method but with the amphithere type in its signature, which the refmap can't map;
    // the mixin is remap = false, so both the dev and the production (SRG) name are listed and whichever exists matches
    @Unique
    private static final String SETUP_ANIM = "setupAnim(Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;FFFFF)V";
    @Unique
    private static final String SETUP_ANIM_SRG = "m_6973_(Lcom/github/alexthe666/iceandfire/entity/EntityAmphithere;FFFFF)V";
    @Unique
    private static final float amphiMod$DEG = Mth.DEG_TO_RAD;
    // Dive pose targets for amphiMod$diveParts(), same order (Ice and Fire's dive progressRotation values)
    @Unique
    private static final float[][] amphiMod$DIVE = {
            {0.2617994F, 0, 0}, {-0.34906584F, 0, 0.34906584F}, {(float) Math.PI / 90, 0, 0}, {0.13962634F, 0, 0}, {0, 0, 0},
            {(float) Math.PI / 90, 0, 0}, {0.55850536F, 0, 1.6580628F}, {0.2617994F, 0, 0}, {1.4835298F, 0, 0},
            {-0.34906584F, 0, -0.34906584F}, {1.4835298F, 0, 0}, {0.06981317F, 0, 0}, {0.13962634F, 0, 0}, {0.06981317F, 0, 0},
            {0.55850536F, 0, -1.6580628F}};
    /** The new animation is used for this frame (an amphithere with animation state). */
    @Unique
    private boolean amphiMod$animActive;
    /** Idle tail sway scale: none in the air, full on the ground. */
    @Unique
    private float amphiMod$swayKeep = 1.0F;

    @Unique
    private ModelAmphithere amphiMod$self() {
        return (ModelAmphithere) (Object) this;
    }

    // Nudges the mirrored left tail fin into line with the right one. 1.12.2 set a new position after the default pose was
    // saved (so it was reset every frame) plus small default offsets of 0.01075 / 0.03735 blocks; the offsets are what
    // actually showed, so they're applied here as 0.172 / 0.598 pixels on the default position
    @Inject(method = "<init>", at = @At("RETURN"))
    private void amphiMod$fixTailFin(CallbackInfo ci) {
        AdvancedModelBox fin = amphiMod$self().TailL2;
        fin.setPos(fin.defaultPositionX + 0.172F, fin.defaultPositionY, fin.defaultPositionZ + 0.598F);
        fin.defaultPositionX = fin.rotationPointX;
        fin.defaultPositionY = fin.rotationPointY;
        fin.defaultPositionZ = fin.rotationPointZ;
    }

    @Inject(method = {SETUP_ANIM, SETUP_ANIM_SRG}, at = @At("HEAD"))
    private void amphiMod$beginAnim(EntityAmphithere amphi, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        this.amphiMod$animActive = amphi instanceof IAmphiAnimState;
        this.amphiMod$swayKeep = 1.0F;
        if (this.amphiMod$animActive) {
            float pt = Mth.clamp(ageInTicks - amphi.tickCount, 0.0F, 1.0F);
            this.amphiMod$swayKeep = Mth.clamp(((IAmphiAnimState) amphi).amphiAnim$groundProgress(pt) / 20.0F, 0.0F, 1.0F);
        }
    }

    // Ice and Fire's plain sine wing flap is replaced by the wing beat below
    @Redirect(method = {SETUP_ANIM, SETUP_ANIM_SRG},
            at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/client/model/ModelAmphithere;flap(Lcom/github/alexthe666/citadel/client/model/AdvancedModelBox;FFZFFFF)V"))
    private void amphiMod$skipBaseFlap(ModelAmphithere self, AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        if (!this.amphiMod$animActive) {
            self.flap(box, speed, degree, invert, offset, weight, flap, flapAmount);
        }
    }

    // The idle tail sway only plays on the ground (in the air the tail follows the bob and turns instead)
    @Redirect(method = {SETUP_ANIM, SETUP_ANIM_SRG},
            at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/client/model/ModelAmphithere;chainSwing([Lcom/github/alexthe666/citadel/client/model/AdvancedModelBox;FFDFF)V"))
    private void amphiMod$calmTailSway(ModelAmphithere self, AdvancedModelBox[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        boolean idleTail = this.amphiMod$animActive && boxes.length == 4 && boxes[0] == self.Tail1;
        self.chainSwing(boxes, speed, degree, rootOffset, swing, idleTail ? swingAmount * this.amphiMod$swayKeep : swingAmount);
    }

    @Inject(method = {SETUP_ANIM, SETUP_ANIM_SRG}, at = @At("TAIL"))
    private void amphiMod$endAnim(EntityAmphithere amphi, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        amphiMod$nameAndGender(amphi);
        if (this.amphiMod$animActive) {
            amphiMod$flightAnimation(amphi, ageInTicks);
        }
    }

    @Unique
    private void amphiMod$nameAndGender(EntityAmphithere amphi) {
        ModelAmphithere m = amphiMod$self();
        String name = amphi.hasCustomName() ? ChatFormatting.stripFormatting(amphi.getCustomName().getString().toLowerCase()) : "";
        // The model is shared by every amphithere, so each part is set back every frame
        boolean noWings = "shrimp".equals(name);
        m.WingL.showModel = !noWings;
        m.WingR.showModel = !noWings;
        boolean bald = "bald".equals(name) && !amphi.isBaby();
        for (AdvancedModelBox crest : new AdvancedModelBox[]{m.CrestL1, m.CrestL2, m.CrestL3, m.CrestR1, m.CrestR2, m.CrestR3, m.CrestLB, m.CrestRB}) {
            crest.showModel = !bald;
        }
        m.Neck3.showModel = !"h-2".equals(name);
        if (AmphiConfig.MALE_AND_FEMALE.get() && !amphi.isBaby() && ((IAmphithereData) amphi).amphiMod$isFemale()) {
            m.TailR2.rotateAngleY -= 0.12F;
            m.TailL2.rotateAngleY += 0.12F;
        }
    }

    @Unique
    private void amphiMod$flightAnimation(EntityAmphithere amphi, float ageInTicks) {
        ModelAmphithere m = amphiMod$self();
        IAmphiAnimState st = (IAmphiAnimState) amphi;
        float pt = Mth.clamp(ageInTicks - amphi.tickCount, 0.0F, 1.0F);
        float rawAmt = st.amphiAnim$flapProgress(pt) / 10.0F;
        float ground = st.amphiAnim$groundProgress(pt);
        boolean buffersOn = amphi.groundProgress <= 0.0F && amphi.getAnimation() != EntityAmphithere.ANIMATION_WING_BLAST && !amphi.onGround();

        // Ice and Fire posed the ground pose with this tick's progress; top it up to the interpolated value
        float groundDelta = ground - amphi.groundProgress;
        if (groundDelta != 0.0F) {
            amphiMod$groundPose(m, groundDelta);
        }
        // Extra dive pose: the larger of Ice and Fire's dive progress and the pitch-driven dive blend
        float extraDive = Math.max(st.amphiAnim$diveProgress(pt), 20.0F * st.amphiAnim$diveBlend(pt)) - amphi.diveProgress;
        if (extraDive != 0.0F) {
            AdvancedModelBox[] parts = amphiMod$diveParts(m);
            for (int i = 0; i < parts.length; i++) {
                amphiMod$progressRotation(parts[i], extraDive, amphiMod$DIVE[i]);
            }
        }

        float amt = rawAmt * st.amphiAnim$flapKeep(pt);
        if (amt > 0.0F) {
            // Wing beat: each wing section a little behind the one before, folding on the upstroke
            float ph = AmphiAnimMath.wrap01(ageInTicks / AmphiAnimMath.BEAT_TICKS);
            float lag = 0.05F;
            float s1 = AmphiAnimMath.stroke(ph);
            float s2 = AmphiAnimMath.stroke(ph - lag);
            float s3 = AmphiAnimMath.stroke(ph - 2.0F * lag);
            float fold = AmphiAnimMath.fold(ph - lag * 0.5F) * amt;
            float arm = (s1 > 0.0F ? 1.08F : 0.62F) * amt * s1;
            m.WingL.rotateAngleZ -= arm;
            m.WingR.rotateAngleZ += arm;
            float fore = -0.3F * amt * s2 + 0.55F * fold;
            m.WingL2.rotateAngleZ += fore;
            m.WingR2.rotateAngleZ -= fore;
            m.WingL2.rotateAngleX += 0.25F * fold;
            m.WingR2.rotateAngleX += 0.25F * fold;
            float hand = -0.22F * amt * s3 + 0.25F * fold;
            m.WingL3.rotateAngleZ += hand;
            m.WingR3.rotateAngleZ -= hand;
            m.WingL3.rotateAngleX += 0.85F * fold;
            m.WingR3.rotateAngleX += 0.85F * fold;
            AdvancedModelBox[] fl = {m.FingerL1, m.FingerL2, m.FingerL3, m.FingerL4};
            AdvancedModelBox[] fr = {m.FingerR1, m.FingerR2, m.FingerR3, m.FingerR4};
            for (int i = 0; i < 4; i++) {
                float close = 0.85F * fold;
                float flick = 0.1F * amt * s3 * (0.6F + 0.15F * i);
                fl[i].rotateAngleX -= fl[i].defaultRotationX * close;
                fr[i].rotateAngleX -= fr[i].defaultRotationX * close;
                fl[i].rotateAngleZ -= flick;
                fr[i].rotateAngleZ += flick;
            }
            // Body bob with the stroke, the neck counter-moving so the head stays steadier
            float hb = AmphiAnimMath.stroke(ph - 0.08F);
            m.BodyUpper.rotationPointY += 2.2F * amt * hb;
            m.BodyUpper.rotateAngleX += 0.035F * amt * hb;
            m.Neck1.rotateAngleX -= 0.06F * amt * hb;
            m.Neck2.rotateAngleX += 0.025F * amt * hb;
            amphiMod$syncedPieces(m, ph, amt);
        }
        // Tail follows the body bob and the turns, segment by segment
        AdvancedModelBox[] tail = {m.Tail1, m.Tail2, m.Tail3, m.Tail4, m.Club};
        for (int i = 0; i < tail.length; i++) {
            tail[i].rotateAngleX += st.amphiAnim$bobTail(i, pt) * amphiMod$DEG;
            tail[i].rotateAngleY += st.amphiAnim$turnTail(i, pt) * amphiMod$DEG;
        }
        if (buffersOn) {
            // Bank roll and body pitch (these replace Ice and Fire's roll and pitch buffers)
            m.BodyUpper.rotateAngleZ += st.amphiAnim$bankRoll(pt) * amphiMod$DEG;
            m.BodyUpper.rotateAngleX += st.amphiAnim$bodyPitch(pt) * amphiMod$DEG;
        }
        float lead = st.amphiAnim$neckLead(pt) * amphiMod$DEG;
        m.Neck1.rotateAngleY += lead * 0.3F;
        m.Neck2.rotateAngleY += lead * 0.35F;
        m.Neck3.rotateAngleY += lead * 0.35F;
    }

    @Unique
    private static AdvancedModelBox[] amphiMod$diveParts(ModelAmphithere m) {
        return new AdvancedModelBox[]{m.FingerR4, m.WingL2, m.FingerR1, m.FingerL3, m.WingR21, m.FingerL1, m.WingR, m.FingerL4,
                m.WingR3, m.WingR2, m.WingL3, m.FingerL2, m.FingerR3, m.FingerR2, m.WingL};
    }

    /** Wing sweep and finger ripple timed with the beat. */
    @Unique
    private static void amphiMod$syncedPieces(ModelAmphithere m, float ph, float amt) {
        float df = 0.5F * amt;
        float fc = df * 0.75F * 2.2F;
        amphiMod$swing(m.WingL, ph, df * 0.45F * 2.3F, true, 0.0F, 0.15F);
        amphiMod$swing(m.WingR, ph, df * 0.45F * 2.3F, false, 0.0F, 0.15F);
        amphiMod$swing(m.WingL2, ph, df * 0.4F * 2.3F, true, -0.25F, 0.25F);
        amphiMod$swing(m.WingR2, ph, df * 0.4F * 2.3F, false, -0.25F, 0.25F);
        amphiMod$swing(m.WingL3, ph, df * 0.35F * 2.3F, true, -0.5F, 0.35F);
        amphiMod$swing(m.WingR3, ph, df * 0.35F * 2.3F, false, -0.5F, 0.35F);
        AdvancedModelBox[] fl = {m.FingerL1, m.FingerL2, m.FingerL3, m.FingerL4};
        AdvancedModelBox[] fr = {m.FingerR1, m.FingerR2, m.FingerR3, m.FingerR4};
        float[] w = {0.3F, 0.4F, 0.5F, 0.6F};
        for (int i = 0; i < 4; i++) {
            fl[i].rotateAngleZ += amphiMod$wave(ph, -1.5F) * fc * w[i];
            fr[i].rotateAngleZ -= amphiMod$wave(ph, -1.5F) * fc * w[i];
        }
    }

    @Unique
    private static float amphiMod$wave(float ph, float offsetRadians) {
        return -AmphiAnimMath.stroke(ph + offsetRadians / (Mth.PI * 2.0F));
    }

    @Unique
    private static void amphiMod$swing(AdvancedModelBox box, float ph, float degree, boolean invert, float offset, float weight) {
        float m = amphiMod$wave(ph, offset) * degree;
        box.rotateAngleY += (invert ? -m : m) * weight;
    }

    /** Ice and Fire's ground pose, applied for an extra amount of progress p (can be negative). */
    @Unique
    private static void amphiMod$groundPose(ModelAmphithere m, float p) {
        amphiMod$progressRotation(m.Tail1, p, new float[]{-0.045553092F, 0, 0});
        amphiMod$progressRotation(m.CrestR2, p, new float[]{1.7453293F, -0.61086524F, -0.31415927F});
        amphiMod$progressRotation(m.FingerR4, p, new float[]{-0.2617994F, 0, 0});
        amphiMod$progressRotation(m.TailL1, p, new float[]{1.3962634F, -0.06981317F, 0});
        amphiMod$progressRotation(m.Neck2, p, new float[]{-0.4553564F, 0, 0});
        amphiMod$progressRotation(m.FingerR3, p, new float[]{-0.13962634F, 0, 0});
        amphiMod$progressRotation(m.FingerL1, p, new float[]{0.13962634F, 0, 0});
        amphiMod$progressRotation(m.WingR, p, new float[]{0.08726646F, 0, 1.134464F});
        amphiMod$progressRotation(m.FingerL4, p, new float[]{-0.2617994F, 0, 0});
        amphiMod$progressRotation(m.Teeth2, p, new float[]{0, 0, 0});
        amphiMod$progressRotation(m.FingerR2, p, new float[]{0, 0, 0});
        amphiMod$progressRotation(m.Beak, p, new float[]{0.6981317F, 0, 0});
        amphiMod$progressRotation(m.BodyUpper, p, new float[]{0, 0, 0});
        amphiMod$progressRotation(m.Club, p, new float[]{0.18203785F, 0, 0});
        amphiMod$progressRotation(m.CrestLB, p, new float[]{1.7453293F, 0.27314404F, 0});
        amphiMod$progressRotation(m.FingerR1, p, new float[]{0.13962634F, 0, 0});
        amphiMod$progressRotation(m.HeadFront, p, new float[]{-0.22759093F, 0, 0});
        amphiMod$progressRotation(m.Neck1, p, new float[]{-0.18203785F, 0, -0.045553092F});
        amphiMod$progressRotation(m.FingerL2, p, new float[]{0, 0, 0});
        amphiMod$progressRotation(m.WingR3, p, new float[]{0.12217305F, -0.17453292F, -0.12217305F});
        amphiMod$progressRotation(m.Teeth1, p, new float[]{0, 0, 0});
        amphiMod$progressRotation(m.Tail4, p, new float[]{-0.045553092F, 0, 0});
        amphiMod$progressRotation(m.WingL3, p, new float[]{0.12217305F, 0.17453292F, 0.12217305F});
        amphiMod$progressRotation(m.Jaw, p, new float[]{-0.31869712F, 0, 0});
        amphiMod$progressRotation(m.Tail2, p, new float[]{0.091106184F, 0, 0});
        amphiMod$progressRotation(m.CrestRB, p, new float[]{1.7453293F, -0.27314404F, 0});
        amphiMod$progressRotation(m.BodyLower, p, new float[]{-0.13665928F, 0, 0});
        amphiMod$progressRotation(m.CrestR3, p, new float[]{1.7453293F, -0.87266463F, -0.7285004F});
        amphiMod$progressRotation(m.WingL21, p, new float[]{0, 0, 0});
        amphiMod$progressRotation(m.Neck3, p, new float[]{0.18203785F, 0, 0});
        amphiMod$progressRotation(m.TailL2, p, new float[]{1.3962634F, 0.31869712F, 0});
        amphiMod$progressRotation(m.FingerL3, p, new float[]{-0.13962634F, 0, 0});
        amphiMod$progressRotation(m.CrestL3, p, new float[]{1.7453293F, 0.87266463F, 0.7853982F});
        amphiMod$progressRotation(m.CrestL2, p, new float[]{1.7453293F, 0.61086524F, 0.31415927F});
        amphiMod$progressRotation(m.WingR21, p, new float[]{0, 0, 0});
        amphiMod$progressRotation(m.TailR1, p, new float[]{1.3962634F, 0.06981317F, 0});
        amphiMod$progressRotation(m.Head, p, new float[]{0.8196066F, 0, 0});
        amphiMod$progressRotation(m.WingR2, p, new float[]{1.4835298F, 0, 0.17453292F});
        amphiMod$progressRotation(m.WingL, p, new float[]{0.08726646F, 0, -1.134464F});
        amphiMod$progressRotation(m.CrestR1, p, new float[]{1.7453293F, -0.08726646F, 0});
        amphiMod$progressRotation(m.WingL2, p, new float[]{1.4835298F, 0, -0.17453292F});
        amphiMod$progressRotation(m.Tail3, p, new float[]{0.045553092F, 0, 0});
        amphiMod$progressRotation(m.TailR2, p, new float[]{1.3962634F, -0.31869712F, 0});
        amphiMod$progressRotation(m.CrestL1, p, new float[]{1.7453293F, 0.08726646F, 0});
        m.BodyUpper.rotationPointY += p * (18.0F - m.BodyUpper.defaultPositionY) / 20.0F;
    }

    @Unique
    private static void amphiMod$progressRotation(AdvancedModelBox box, float progress, float[] to) {
        box.rotateAngleX += progress * (to[0] - box.defaultRotationX) / 20.0F;
        box.rotateAngleY += progress * (to[1] - box.defaultRotationY) / 20.0F;
        box.rotateAngleZ += progress * (to[2] - box.defaultRotationZ) / 20.0F;
    }
}
