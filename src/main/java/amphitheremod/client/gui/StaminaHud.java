package amphitheremod.client.gui;

import amphitheremod.config.AmphiConfig;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/**
 * Stamina bar shown while riding your tamed amphithere: framed bar scaled to max stamina (up to 150 pixels), a tick
 * every 25 stamina and the number in the middle. Same textures and layout as 1.12.2.
 */
public class StaminaHud implements IGuiOverlay {
    private static final ResourceLocation LEFT = tex("left");
    private static final ResourceLocation MIDDLE = tex("middle");
    private static final ResourceLocation RIGHT = tex("right");
    private static final ResourceLocation FILLER = tex("bar_filler");
    private static final ResourceLocation SEGMENT_LINE = tex("segment_line");
    private static final int EDGE = 14;
    private static final int HEIGHT = 14;
    private static final int MAX_WIDTH = 150;
    private static final float PIXELS_PER_STAMINA = 0.75F * 0.8F;
    private static final int FILL_OFFSET = 3;
    private static final int FILL_HEIGHT = 8;
    private static final int STAMINA_PER_SEGMENT = 25;

    private static ResourceLocation tex(String name) {
        return new ResourceLocation("amphitheremod", "textures/gui/stamina_bar/" + name + ".png");
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics g, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || !AmphiConfig.ENABLE_STAMINA.get()
                || !(mc.player.getVehicle() instanceof EntityAmphithere amphi) || !amphi.isTame()) {
            return;
        }
        IAmphithereData data = (IAmphithereData) amphi;
        float stamina = data.amphiMod$getStamina();
        float maxStamina = data.amphiMod$getMaxStamina();
        if (maxStamina <= 0.0F) {
            return;
        }
        int totalWidth = Math.min(EDGE * 2 + (int) (maxStamina * PIXELS_PER_STAMINA), MAX_WIDTH);
        int middleWidth = Math.max(0, totalWidth - EDGE * 2);
        int x = (screenWidth - totalWidth) / 2;
        // 1.12.2 put it 130 below the centre; keep that but never over the hotbar on small screens
        int y = Math.min(screenHeight / 2 + 130, screenHeight - 60);

        g.blit(LEFT, x, y, 0, 0, EDGE, HEIGHT, EDGE, HEIGHT);
        if (middleWidth > 0) {
            g.blit(MIDDLE, x + EDGE, y, 0, 0, middleWidth, HEIGHT, middleWidth, HEIGHT);
        }
        g.blit(RIGHT, x + EDGE + middleWidth, y, 0, 0, EDGE, HEIGHT, EDGE, HEIGHT);

        int fillX = x + FILL_OFFSET;
        int fillY = y + FILL_OFFSET;
        int maxFill = totalWidth - FILL_OFFSET * 2;
        int fill = (int) (stamina / maxStamina * maxFill);
        if (fill > 0) {
            g.blit(FILLER, fillX, fillY, 0, 0, fill, FILL_HEIGHT, fill, FILL_HEIGHT);
        }
        // Segment ticks inside the filled part
        int segments = (int) (maxStamina / STAMINA_PER_SEGMENT);
        for (int i = 1; i <= segments; i++) {
            if (i * STAMINA_PER_SEGMENT >= stamina) {
                break;
            }
            int lineX = fillX + (int) ((float) (i * STAMINA_PER_SEGMENT) / maxStamina * maxFill);
            g.blit(SEGMENT_LINE, lineX, fillY, 0, 0, 1, FILL_HEIGHT, 1, FILL_HEIGHT);
        }
        String text = String.format("%.1f", stamina);
        g.drawString(mc.font, text, x + (totalWidth - mc.font.width(text)) / 2, y + (HEIGHT - mc.font.lineHeight) / 2 + 1, 0xFFFFFF, true);
    }
}
