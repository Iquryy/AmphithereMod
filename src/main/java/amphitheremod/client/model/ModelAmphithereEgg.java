package amphitheremod.client.model;

import amphitheremod.entity.EntityAmphithereEgg;
import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import net.minecraft.entity.Entity;

public class ModelAmphithereEgg extends ModelDragonEgg {

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        if (entityIn instanceof EntityAmphithereEgg) {
            EntityAmphithereEgg egg = (EntityAmphithereEgg) entityIn;
            if (egg.isWarm()) {
                float wobbleSpeed = 0.25F;
                float wobbleAmount = 5.0F;
                this.Egg1.rotateAngleX = (float) Math.toRadians(Math.sin(ageInTicks * wobbleSpeed) * wobbleAmount);
                this.Egg1.rotateAngleZ = (float) Math.toRadians(Math.cos(ageInTicks * (wobbleSpeed * 0.75F)) * wobbleAmount);
            }
        }
    }
}