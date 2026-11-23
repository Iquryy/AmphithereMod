package amphitheremod.client.gui;

import amphitheremod.compat.SimpleHatsCompat;
import amphitheremod.config.AmphiConfig;
import amphitheremod.menu.AmphithereMenu;
import amphitheremod.network.AmphiNetwork;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Amphithere inventory: the amphithere following the mouse, its health, stamina and gender, the gear slots and a button
 * that cycles its command (wander / sit / follow).
 */
public class AmphithereScreen extends AbstractContainerScreen<AmphithereMenu> {
    // One background per combination of armor slots and hat slot, as in 1.12.2 (the "classyhats" textures draw the hat slot)
    private static final ResourceLocation TEXTURE_BOTH = new ResourceLocation("amphitheremod", "textures/gui/amphithere.png");
    private static final ResourceLocation TEXTURE_ARMOR = new ResourceLocation("amphitheremod", "textures/gui/amphithere_no_classyhats.png");
    private static final ResourceLocation TEXTURE_HAT = new ResourceLocation("amphitheremod", "textures/gui/amphithere_no_armor.png");
    private static final ResourceLocation TEXTURE_NEITHER = new ResourceLocation("amphitheremod", "textures/gui/amphithere_no_both.png");
    private Button commandButton;

    public AmphithereScreen(AmphithereMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageHeight = 214;
    }

    @Override
    protected void init() {
        super.init();
        EntityAmphithere amphi = this.menu.amphithere;
        if (amphi != null && amphi.isTame() && this.minecraft != null && amphi.isOwnedBy(this.minecraft.player)) {
            this.commandButton = this.addRenderableWidget(Button.builder(Component.empty(), b ->
                            AmphiNetwork.CHANNEL.sendToServer(new AmphiNetwork.ChangeCommand(amphi.getId(), amphi.getCommand() + 1)))
                    .bounds(this.leftPos + this.imageWidth / 2 - 40, this.topPos + 109, 80, 20).build());
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.commandButton != null && this.menu.amphithere != null) {
            this.commandButton.setMessage(Component.translatable("gui.amphitheremod.command." + this.menu.amphithere.getCommand()));
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);
        super.render(g, mouseX, mouseY, partialTick);
        this.renderTooltip(g, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        boolean armor = AmphiConfig.ENABLE_ARMOR.get();
        boolean hat = SimpleHatsCompat.isLoaded();
        ResourceLocation texture = armor ? (hat ? TEXTURE_BOTH : TEXTURE_ARMOR) : (hat ? TEXTURE_HAT : TEXTURE_NEITHER);
        g.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.amphithere != null) {
            // Same spot and size as 1.12.2 (25 scaled by 0.6); the last two values are where the "eyes" look from
            InventoryScreen.renderEntityInInventoryFollowsMouse(g, this.leftPos + 75, this.topPos + 61, 15,
                    this.leftPos + 88 - mouseX, this.topPos + 25 - mouseY, this.menu.amphithere);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        EntityAmphithere amphi = this.menu.amphithere;
        if (amphi == null) {
            return;
        }
        IAmphithereData data = (IAmphithereData) amphi;
        centered(g, amphi.getDisplayName(), 6);
        centered(g, Component.translatable("amphitheremod.amphithere.health").append(" ")
                .append(Component.literal(String.valueOf((int) amphi.getHealth())).withStyle(ChatFormatting.RED)).append("/")
                .append(Component.literal(String.valueOf((int) amphi.getMaxHealth())).withStyle(ChatFormatting.RED)), 75);
        int y = 84;
        if (AmphiConfig.ENABLE_STAMINA.get()) {
            centered(g, Component.translatable("amphitheremod.amphithere.stamina").append(" ")
                    .append(Component.literal(String.format("%.0f", data.amphiMod$getStamina())).withStyle(ChatFormatting.YELLOW)).append("/")
                    .append(Component.literal(String.format("%.0f", data.amphiMod$getMaxStamina())).withStyle(ChatFormatting.YELLOW)), y);
            y += 9;
        }
        if (AmphiConfig.MALE_AND_FEMALE.get()) {
            centered(g, Component.translatable("amphitheremod.amphithere.gender").append(" ")
                    .append(Component.translatable(data.amphiMod$isFemale() ? "amphitheremod.amphithere.gender.female" : "amphitheremod.amphithere.gender.male")), y);
        }
    }

    private void centered(GuiGraphics g, Component text, int y) {
        g.drawString(this.font, text, this.imageWidth / 2 - this.font.width(text) / 2, y, 0x404040, false);
    }
}
