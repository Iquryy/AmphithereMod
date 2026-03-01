package amphitheremod.mixin.client;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModelAmphithere.class)
public class AmphithereModel {
    @Shadow(remap = false)
    public AdvancedModelRenderer TailL2, TailR2, WingL, WingR, CrestL1, CrestL2, CrestR2, CrestR1, CrestR3, CrestL3, CrestRB, CrestLB, Neck3, WingR3, WingL3, FingerR4, FingerR3, FingerR2, FingerR1, FingerL4, FingerL3, FingerL2, FingerL1, WingR2, WingL2;

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

    // Fix Random Stutter
    @Unique
    private void amphithereMod$flap(AdvancedModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        float motion = (float) (Math.cos(f * speed + offset) * degree * f1);
        if (invert) {
            motion = -motion;
        }
        box.rotateAngleZ += motion * weight;
    }

    @Unique
    private void amphithereMod$swing(AdvancedModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        float motion = (float) (Math.cos(f * speed + offset) * degree * f1);
        if (invert) {
            motion = -motion;
        }
        box.rotateAngleY += motion * weight;
    }

    @Unique
    private static final float WING_BEND_STRENGTH = 1.75F;
    @Unique
    private static final float WING_TWIST_STRENGTH = 2.3F;
    @Unique
    private static final float FINGER_CURL_STRENGTH = 2.2F;

    /*
    @Unique
    private static final float WING_BEND_STRENGTH = 1.5F;
    @Unique
    private static final float WING_TWIST_STRENGTH = 1.8F;
    @Unique
    private static final float FINGER_CURL_STRENGTH = 2.0F;
    * */

    @Inject(method = "setRotationAngles", at = @At("TAIL"), order = 10)
    private void addRealisticWingAnimations(float f, float f1, float f2, float f3, float f4, float f5, Entity entity, CallbackInfo ci) {
        EntityAmphithere amphithere = (EntityAmphithere) entity;
        float speed_fly = 0.2F;
        float degree_flap = 0.5F * (amphithere.flapProgress / 10.0F);
        float fingerCurl = degree_flap * 0.75F * FINGER_CURL_STRENGTH;

        this.amphithereMod$flap(this.WingL, speed_fly, degree_flap * 0.75F * WING_BEND_STRENGTH, true, 0.0F, 0.0F, f2, 1.0F);
        this.amphithereMod$flap(this.WingR, speed_fly, degree_flap * 0.75F * WING_BEND_STRENGTH, true, 0.0F, 0.0F, f2, 1.0F);

        this.amphithereMod$flap(this.WingL2, speed_fly, degree_flap * 0.65F * WING_BEND_STRENGTH, true, -0.25F, 0.0F, f2, 1.0F);
        this.amphithereMod$flap(this.WingR2, speed_fly, degree_flap * 0.65F * WING_BEND_STRENGTH, true, -0.25F, 0.0F, f2, 1.0F);

        this.amphithereMod$flap(this.WingL3, speed_fly, degree_flap * 0.55F * WING_BEND_STRENGTH, true, -0.5F, 0.0F, f2, 1.0F);
        this.amphithereMod$flap(this.WingR3, speed_fly, degree_flap * 0.55F * WING_BEND_STRENGTH, true, -0.5F, 0.0F, f2, 1.0F);

        this.amphithereMod$swing(this.WingL, speed_fly, degree_flap * 0.45F * WING_TWIST_STRENGTH, true, 0.0F, 0.15F, f2, 1.0F);
        this.amphithereMod$swing(this.WingR, speed_fly, degree_flap * 0.45F * WING_TWIST_STRENGTH, false, 0.0F, 0.15F, f2, 1.0F);

        this.amphithereMod$swing(this.WingL2, speed_fly, degree_flap * 0.40F * WING_TWIST_STRENGTH, true, -0.25F, 0.25F, f2, 1.0F);
        this.amphithereMod$swing(this.WingR2, speed_fly, degree_flap * 0.40F * WING_TWIST_STRENGTH, false, -0.25F, 0.25F, f2, 1.0F);

        this.amphithereMod$swing(this.WingL3, speed_fly, degree_flap * 0.35F * WING_TWIST_STRENGTH, true, -0.5F, 0.35F, f2, 1.0F);
        this.amphithereMod$swing(this.WingR3, speed_fly, degree_flap * 0.35F * WING_TWIST_STRENGTH, false, -0.5F, 0.35F, f2, 1.0F);

        this.amphithereMod$flap(this.FingerL1, speed_fly, fingerCurl * 0.3F, false, -1.5F, 1.0F, f2, 1F);
        this.amphithereMod$flap(this.FingerR1, speed_fly, fingerCurl * 0.3F, true, -1.5F, 1.0F, f2, 1F);
        this.amphithereMod$flap(this.FingerL2, speed_fly, fingerCurl * 0.4F, false, -1.5F, 1.0F, f2, 1F);
        this.amphithereMod$flap(this.FingerR2, speed_fly, fingerCurl * 0.4F, true, -1.5F, 1.0F, f2, 1F);
        this.amphithereMod$flap(this.FingerL3, speed_fly, fingerCurl * 0.5F, false, -1.5F, 1.0F, f2, 1F);
        this.amphithereMod$flap(this.FingerR3, speed_fly, fingerCurl * 0.5F, true, -1.5F, 1.0F, f2, 1F);
        this.amphithereMod$flap(this.FingerL4, speed_fly, fingerCurl * 0.6F, false, -1.5F, 1.0F, f2, 1F);
        this.amphithereMod$flap(this.FingerR4, speed_fly, fingerCurl * 0.6F, true, -1.5F, 1.0F, f2, 1F);
    }
}
