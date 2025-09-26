package amphitheremod.client.layer;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import static amphitheremod.util.Refs.*;

@SideOnly(Side.CLIENT)
public class LayerAmphithereGlow extends AbstractAmphithereLayer {
    public LayerAmphithereGlow(RenderLiving<EntityAmphithere> rendererIn) {
        super(rendererIn);
    }

    @Override protected ResourceLocation getTextureToBind(EntityAmphithere amphithere) {
        int amphiVariant = amphithere.getVariant();
        EnumAmphiType.Glow glow = EnumAmphiType.Glow.SHIVAXI_GLOW;
        if(amphiVariant < EnumAmphiType.values().length)
            glow = EnumAmphiType.values()[amphiVariant].getGlow();

        ResourceLocation glowTexture = EMPTY;
        switch (glow){
            case NONE: return null;
            case SHIVAXI_GLOW: glowTexture = SHIVAXI_GLOW; break;
            case IQURY_GLOW: glowTexture = IQURY_GLOW; break;
            case BLACKEAGLE_GLOW: glowTexture = BLACKEAGLE_GLOW; break;
        }
        return glowTexture;
    }

    @Override
    public void doRenderLayer(EntityAmphithere amphithere, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (amphithere.isInvisible()) return;
        float overallBrightness = 0.45f;
        float pulseAlpha = getGlowBrightness(partialTicks, amphithere);
        if (pulseAlpha <= 0) return;
        float finalBrightness = overallBrightness * pulseAlpha;
        ResourceLocation texture = this.getTextureToBind(amphithere);
        if (texture == null || texture == EMPTY) return;
        this.renderer.bindTexture(texture);
        GlStateManager.pushMatrix();
        boolean isChild = amphithere.isChild();
        if (isChild) GlStateManager.translate(0.0F, -0.2F, 0.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        boolean wasDepthMaskEnabled = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GlStateManager.depthMask(!amphithere.isInvisible());
        float worldBrightnessX = OpenGlHelper.lastBrightnessX;
        float worldBrightnessY = OpenGlHelper.lastBrightnessY;
        int fullbrightCoord = 0xF000F0;
        float fullbrightX = (float)(fullbrightCoord % 65536);
        float fullbrightY = (float)(fullbrightCoord / 65536);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, fullbrightX * finalBrightness, fullbrightY * finalBrightness);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.renderer.getMainModel().render(amphithere, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, worldBrightnessX, worldBrightnessY);
        GlStateManager.depthMask(wasDepthMaskEnabled);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public float getGlowBrightness(float partialTicks, EntityAmphithere amphi) {
        float fadeDurationTicks = 90.0f;
        float amphiExisted = amphi.ticksExisted;
        float time = amphiExisted + partialTicks;
        float frequency = (float) (2.0 * Math.PI / fadeDurationTicks);
        return (float) (Math.sin(time * frequency) + 1.0) / 2.0f;
    }
}