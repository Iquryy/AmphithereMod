package amphitheremod.client.layer;

import amphitheremod.compat.SimpleHatsCompat;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Draws a SimpleHats hat from the amphithere's off-hand slot on its head (1.12.2's ClassyHats layer). The pose stack is
 * walked down the model from the body to the head, the same parts 1.12.2 used, so the hat follows the neck and head
 * animation; the hat's own position and size are 1.12.2's.
 */
public class AmphiHatLayer extends RenderLayer<EntityAmphithere, ModelAmphithere> {
    public AmphiHatLayer(RenderLayerParent<EntityAmphithere, ModelAmphithere> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, EntityAmphithere amphi, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack hat = amphi.getOffhandItem();
        if (amphi.isInvisible() || !SimpleHatsCompat.isHat(hat)) {
            return;
        }
        ModelAmphithere model = this.getParentModel();
        // "H-2" is drawn headless (AmphiModelMixin hides the neck and head), so there's nothing to put a hat on
        if (!model.Neck3.showModel) {
            return;
        }
        pose.pushPose();
        AdvancedModelBox[] chain = {model.BodyUpper, model.Neck1, model.Neck2, model.Neck3, model.Head};
        for (int i = 0; i < chain.length; i++) {
            AdvancedModelBox part = chain[i];
            part.translateAndRotate(pose);
            // A part's scale only carries to its children when it scales them (like Citadel's own render does), so it's
            // undone here otherwise. The head keeps its scale so the hat matches a baby's bigger head.
            if (i < chain.length - 1 && !part.scaleChildren) {
                pose.scale(1.0F / Math.max(part.scaleX, 1.0E-4F), 1.0F / Math.max(part.scaleY, 1.0E-4F), 1.0F / Math.max(part.scaleZ, 1.0E-4F));
            }
        }
        // 1.12.2's hat placement on the head
        pose.mulPose(Axis.XP.rotationDegrees(180.0F));
        pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        pose.translate(0.0F, 0.07F, -0.004F);
        pose.scale(0.31F, 0.31F, 0.31F);
        Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer()
                .renderItem(amphi, hat, ItemDisplayContext.HEAD, false, pose, buffers, light);
        pose.popPose();
    }
}
