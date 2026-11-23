package amphitheremod.client.render;

import amphitheremod.entity.EntityAmphithereEgg;
import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/** Amphithere egg: Ice and Fire's dragon egg model at 60% size with the variant's egg texture; wobbles while warm. */
public class RenderAmphithereEgg extends LivingEntityRenderer<EntityAmphithereEgg, RenderAmphithereEgg.Model> {
    public RenderAmphithereEgg(EntityRendererProvider.Context context) {
        super(context, new Model(), 0.15F);
    }

    @Override
    protected void scale(@NotNull EntityAmphithereEgg egg, PoseStack pose, float partialTick) {
        pose.scale(0.6F, 0.6F, 0.6F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityAmphithereEgg egg) {
        return new ResourceLocation("amphitheremod", "textures/entity/amphithere_egg/" + egg.getVariantType().getTexturePath() + ".png");
    }

    @Override
    protected boolean shouldShowName(@NotNull EntityAmphithereEgg egg) {
        return egg.hasCustomName() && super.shouldShowName(egg);
    }

    public static class Model extends ModelDragonEgg<EntityAmphithereEgg> {
        @Override
        public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.resetToDefaultPose();
            this.Egg1.setPos(0.0F, 19.6F, 0.0F);
            this.Egg4.setPos(0.0F, -0.9F, 0.0F);
            if (entity instanceof EntityAmphithereEgg egg && egg.isWarm()) {
                // 5 degree wobble on two axes at slightly different speeds
                this.Egg1.rotateAngleX = (float) Math.toRadians(Math.sin(ageInTicks * 0.25F) * 5.0);
                this.Egg1.rotateAngleZ = (float) Math.toRadians(Math.cos(ageInTicks * 0.1875F) * 5.0);
            }
        }
    }
}
