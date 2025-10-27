package amphitheremod.client.layer;

import amphitheremod.util.enumm.EnumAmphiType;
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
public class LayerAmphithereEyes extends AbstractAmphithereLayer {
    public LayerAmphithereEyes(RenderLiving<EntityAmphithere> rendererIn) {
        super(rendererIn);
    }

    @Override protected ResourceLocation getTextureToBind(EntityAmphithere amphithere) {
        if (amphithere.isBlinking()) return null;
        EnumAmphiType.Eyes eyes = getEyes(amphithere);

        ResourceLocation eyeTexture = EMPTY;
        switch (eyes) {
            case NONE:
            case NORMAL: return null;
            case YELLOW: eyeTexture = YELLOW_EYES; break;
            case PINK: eyeTexture = PINK_EYES; break;
            case LIME: eyeTexture = LIME_EYES; break;
            case LIGHT_BLUE: eyeTexture = LIGHT_BLUE_EYES; break;
            case MAGENTA: eyeTexture = MAGENTA_EYES; break;
            case ORANGE: eyeTexture = ORANGE_EYES; break;
            case PURPLE: eyeTexture = PURPLE_EYES; break;
            case SHIVAXI: eyeTexture = SHIVAXI_EYES; break;
            case IQURY: eyeTexture = IQURY_EYES; break;
            case BLACKEAGLE: eyeTexture = BLACKEAGLE_EYES; break;
            case CRAFTY: eyeTexture = CRAFTY_EYES; break;
        }
        return eyeTexture;
    }

    private static EnumAmphiType.Eyes getEyes(EntityAmphithere amphithere) {
        int amphiVariant = amphithere.getVariant();
        EnumAmphiType.Eyes eyes = EnumAmphiType.Eyes.NORMAL;
        String amphiName = amphithere.getName().toLowerCase();
        if (amphiName != null)
            if (amphiName.equals("risky"))
                eyes = EnumAmphiType.RAINBOW.getEyes();
            else if (amphiVariant < EnumAmphiType.values().length)
                eyes = EnumAmphiType.values()[amphiVariant].getEyes();
        return eyes;
    }

    @Override public void doRenderLayer(EntityAmphithere amphithere, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (amphithere.isInvisible()) return;
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
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        int fullBright = 0xF000F0;
        int j_light = fullBright % 65536;
        int k_light = fullBright / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j_light, (float) k_light);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.renderer.getMainModel().render(amphithere, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        GlStateManager.depthMask(wasDepthMaskEnabled);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}