package amphitheremod.client.layer;

import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.Function;

/**
 * Full-bright additive overlay: glowing eyes (hidden while blinking) and the Shivaxi/Iqury body glow, which pulses on a
 * 90 tick cycle at up to 45% brightness like 1.12.2. Uses the vanilla "eyes" render type (additive, ignores light).
 */
public class AmphiGlowLayer extends RenderLayer<EntityAmphithere, ModelAmphithere> {
    private final Function<EntityAmphithere, ResourceLocation> texture;
    private final boolean pulse;

    public AmphiGlowLayer(RenderLayerParent<EntityAmphithere, ModelAmphithere> parent, Function<EntityAmphithere, ResourceLocation> texture, boolean pulse) {
        super(parent);
        this.texture = texture;
        this.pulse = pulse;
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
        float brightness = 1.0F;
        if (this.pulse) {
            // Additive blending: darker colour = fainter glow
            brightness = 0.45F * (Mth.sin(ageInTicks * Mth.TWO_PI / 90.0F) + 1.0F) / 2.0F;
            if (brightness <= 0.0F) {
                return;
            }
        }
        this.getParentModel().renderToBuffer(pose, buffers.getBuffer(RenderType.eyes(tex)), 0xF000F0, OverlayTexture.NO_OVERLAY,
                brightness, brightness, brightness, 1.0F);
    }
}
