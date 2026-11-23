package amphitheremod.client.layer;

import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

/**
 * Draws the amphithere model again with an overlay texture (markings, wing patterns, armor, beaks...). The model is
 * already posed by the main render, so overlays line up exactly. Translucent so overlay textures with soft edges blend
 * like 1.12.2's alpha blending.
 */
public class AmphiTextureLayer extends RenderLayer<EntityAmphithere, ModelAmphithere> {
    private final Function<EntityAmphithere, ResourceLocation> texture;

    /** texture returns the overlay texture for an amphithere, or null to draw nothing. */
    public AmphiTextureLayer(RenderLayerParent<EntityAmphithere, ModelAmphithere> parent, Function<EntityAmphithere, ResourceLocation> texture) {
        super(parent);
        this.texture = texture;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, EntityAmphithere amphi, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (amphi.isInvisible()) {
            return;
        }
        ResourceLocation tex = this.texture.apply(amphi);
        if (tex == null) {
            return;
        }
        this.getParentModel().renderToBuffer(pose, buffers.getBuffer(RenderType.entityTranslucent(tex)), light,
                LivingEntityRenderer.getOverlayCoords(amphi, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
