package amphitheremod.mixin.client;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelAmphithere.class)
public class AmphithereModel {
    @Shadow(remap = false)
    public AdvancedModelRenderer TailL2, TailR2, WingL, WingR, CrestL1, CrestL2, CrestR2, CrestR1, CrestR3, CrestL3, CrestRB, CrestLB, Neck3;

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

    @Inject(method = "setRotationAngles", at = @At("TAIL"))
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
            if (data == null) return;
            if (amphi.isChild()) return;
            if (data.amphiMod_master$getGender()) {
                this.TailR2.rotateAngleY -= 0.08f;
                this.TailL2.rotateAngleY += 0.08f;
            }
        }
    }

    @ModifyConstant(method = "setRotationAngles", constant = @Constant(floatValue = 0.2F))
    private float hhh(float constant, float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        return 0.26F;
    }
}