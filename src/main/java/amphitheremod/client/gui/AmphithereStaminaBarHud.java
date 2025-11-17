package amphitheremod.client.gui;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AmphithereStaminaBarHud {
    private static final float BAR_SCALE = 0.8F;
    private static final int MAX_BAR_WIDTH_PIXELS = 150;
    private static final float PIXELS_PER_STAMINA = 0.75F;

    private static final ResourceLocation STAMINA_BAR_LEFT = new ResourceLocation("amphitheremod:textures/gui/stamina_bar/left.png");
    private static final ResourceLocation STAMINA_BAR_MIDDLE = new ResourceLocation("amphitheremod:textures/gui/stamina_bar/middle.png");
    private static final ResourceLocation STAMINA_BAR_RIGHT = new ResourceLocation("amphitheremod:textures/gui/stamina_bar/right.png");
    private static final ResourceLocation STAMINA_BAR_FILLER = new ResourceLocation("amphitheremod:textures/gui/stamina_bar/bar_filler.png");
    private static final ResourceLocation STAMINA_BAR_SEGMENT_LINE = new ResourceLocation("amphitheremod:textures/gui/stamina_bar/segment_line.png");

    private static final int BASE_EDGE_WIDTH = 18;
    private static final int BASE_BAR_HEIGHT = 18;
    private static final int BASE_FILLER_HEIGHT = 10;
    private static final int BASE_FILLER_V_OFFSET = 4;
    private static final int BASE_FILLER_H_OFFSET = 4;
    private static final int BASE_SEGMENT_LINE_WIDTH = 2;
    private static final int BASE_SEGMENT_LINE_HEIGHT = 10;
    private static final int STAMINA_PER_SEGMENT = 25;

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
        if(!ConfigHandler.amphiStamina.enableStamina) return;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if (player.isRiding() && player.getRidingEntity() instanceof EntityAmphithere) {
            EntityAmphithere amphi = (EntityAmphithere) player.getRidingEntity();
            if (!amphi.isTamed() || !(amphi instanceof IAmphithereData)) return;
            IAmphithereData data = (IAmphithereData) amphi;
            float currentStamina = data.amphiMod_master$getStamina();
            float maxStamina = data.amphiMod_master$getMaxStamina();
            if (maxStamina <= 0) return;
            int scaledEdgeWidth = (int)(BASE_EDGE_WIDTH * BAR_SCALE);
            int scaledBarHeight = (int)(BASE_BAR_HEIGHT * BAR_SCALE);
            int desiredMiddleWidth = (int) (maxStamina * PIXELS_PER_STAMINA * BAR_SCALE);
            int totalBarWidth = (scaledEdgeWidth * 2) + desiredMiddleWidth;
            totalBarWidth = Math.min(totalBarWidth, MAX_BAR_WIDTH_PIXELS);
            int finalMiddleWidth = totalBarWidth - (scaledEdgeWidth * 2);
            if (finalMiddleWidth < 0) finalMiddleWidth = 0;
            int screenWidth = event.getResolution().getScaledWidth();
            int screenHeight = event.getResolution().getScaledHeight();
            int barX = (screenWidth - totalBarWidth) / 2;
            int barY = (screenHeight / 2) + 130;
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int currentX = barX;
            mc.getTextureManager().bindTexture(STAMINA_BAR_LEFT);
            Gui.drawModalRectWithCustomSizedTexture(currentX, barY, 0, 0, scaledEdgeWidth, scaledBarHeight, scaledEdgeWidth, scaledBarHeight);
            currentX += scaledEdgeWidth;
            if (finalMiddleWidth > 0) {
                mc.getTextureManager().bindTexture(STAMINA_BAR_MIDDLE);
                Gui.drawModalRectWithCustomSizedTexture(currentX, barY, 0, 0, finalMiddleWidth, scaledBarHeight, finalMiddleWidth, scaledBarHeight);
                currentX += finalMiddleWidth;
            }
            mc.getTextureManager().bindTexture(STAMINA_BAR_RIGHT);
            Gui.drawModalRectWithCustomSizedTexture(currentX, barY, 0, 0, scaledEdgeWidth, scaledBarHeight, scaledEdgeWidth, scaledBarHeight);
            int scaledFillerHOffset = (int)(BASE_FILLER_H_OFFSET * BAR_SCALE);
            int scaledFillerVOffset = (int)(BASE_FILLER_V_OFFSET * BAR_SCALE);
            int scaledFillerHeight = (int)(BASE_FILLER_HEIGHT * BAR_SCALE);
            int fillerStartX = barX + scaledFillerHOffset;
            int fillerStartY = barY + scaledFillerVOffset;
            int maxFillWidth = totalBarWidth - (scaledFillerHOffset * 2);
            float staminaPercentage = currentStamina / maxStamina;
            int fillWidth = (int) (staminaPercentage * maxFillWidth);
            if (fillWidth > 0) {
                mc.getTextureManager().bindTexture(STAMINA_BAR_FILLER);
                Gui.drawModalRectWithCustomSizedTexture(fillerStartX, fillerStartY, 0, 0, fillWidth, scaledFillerHeight, fillWidth, scaledFillerHeight);
            }
            mc.getTextureManager().bindTexture(STAMINA_BAR_SEGMENT_LINE);
            int segmentCount = (int) (maxStamina / STAMINA_PER_SEGMENT);
            if (segmentCount > 0) {
                int scaledSegmentLineWidth = (int)(BASE_SEGMENT_LINE_WIDTH * BAR_SCALE);
                int scaledSegmentLineHeight = (int)(BASE_SEGMENT_LINE_HEIGHT * BAR_SCALE);
                for (int i = 1; i <= segmentCount; i++) {
                    if ((i * STAMINA_PER_SEGMENT) < currentStamina) {
                        float segmentPosition = ((float)(i * STAMINA_PER_SEGMENT) / maxStamina);
                        int lineX = fillerStartX + (int)(segmentPosition * maxFillWidth) - (scaledSegmentLineWidth / 2);
                        Gui.drawModalRectWithCustomSizedTexture(lineX, fillerStartY, 0, 0, scaledSegmentLineWidth, scaledSegmentLineHeight, scaledSegmentLineWidth, scaledSegmentLineHeight);
                    }
                }
            }
            String staminaText = String.format("%.1f", currentStamina);
            int textWidth = mc.fontRenderer.getStringWidth(staminaText);
            int textX = barX + (totalBarWidth - textWidth) / 2;
            int textY = barY + ((scaledBarHeight - mc.fontRenderer.FONT_HEIGHT) / 2) + 1;
            mc.fontRenderer.drawStringWithShadow(staminaText, textX, textY, 0xFFFFFF);
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.popMatrix();
        }
    }
}