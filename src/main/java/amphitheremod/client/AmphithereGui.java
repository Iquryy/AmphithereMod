package amphitheremod.client;

import amphitheremod.config.ConfigHandler;
import amphitheremod.network.PacketChangeAmphithereAI;
import amphitheremod.inventory.AmphithereContainer;
import amphitheremod.util.IAmphithereData;
import amphitheremod.util.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import static amphitheremod.AmphithereMod.NETWORK_WRAPPER;
import static amphitheremod.AmphithereMod.modIdWithDot;

public class AmphithereGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ConfigHandler.general.enableAmphithereArmor ? "amphitheremod:textures/gui/amphithere.png" : "amphitheremod:textures/gui/amphithere_no_armor.png");
    private final EntityAmphithere amphithere;
    private float mousePosX;
    private float mousePosY;

    private static final int COMMAND_BUTTON_ID = 1;
    private GuiButton commandButton;

    public AmphithereGui(EntityPlayer player, EntityAmphithere amphithere) {
        super(new AmphithereContainer(amphithere, player));
        this.amphithere = amphithere;
        this.allowUserInput = false;
        this.ySize = 214;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        if (this.amphithere.isTamed() && this.amphithere.isOwner(this.mc.player)) {
            int buttonWidth = 80;
            int buttonHeight = 20;
            int buttonX = this.guiLeft + (this.xSize / 2) - (buttonWidth / 2);
            int buttonY = this.guiTop + 109;
            String commandText = StatCollector.translateToLocal("gui.amphitheremod.command." + this.amphithere.getCommand());
            commandButton = new GuiButton(COMMAND_BUTTON_ID, buttonX, buttonY, buttonWidth, buttonHeight, commandText);
            this.buttonList.add(commandButton);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == COMMAND_BUTTON_ID && button.enabled) {
            NETWORK_WRAPPER.sendToServer(new PacketChangeAmphithereAI(this.amphithere.getEntityId()));
            int currentCommand = this.amphithere.getCommand();
            int nextCommand = (currentCommand < 2) ? currentCommand + 1 : 0;
            this.amphithere.setCommand(nextCommand);
            button.displayString = StatCollector.translateToLocal("gui.amphitheremod.command." + nextCommand);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mousePosX = (float) mouseX;
        this.mousePosY = (float) mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        IAmphithereData amphiData = (IAmphithereData) this.amphithere;
        String name = this.amphithere.hasCustomName() ? this.amphithere.getCustomNameTag() : StatCollector.translateToLocal("entity." + modIdWithDot + "amphithere.name");
        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
        String health = StatCollector.translateToLocal(modIdWithDot + "amphithere.health") + " " + (int) this.amphithere.getHealth() + "/" + (int) this.amphithere.getMaxHealth();
        this.fontRenderer.drawString(health, this.xSize / 2 - this.fontRenderer.getStringWidth(health) / 2, 75, 4210752);
        if (ConfigHandler.general.enableCrystalFeather) {
            String bounded = StatCollector.translateToLocal(modIdWithDot + "amphithere.bounded") + " " + StatCollector.translateToLocal(amphiData.amphiMod_master$getBounded() ? modIdWithDot + "amphithere.bounded.true" : modIdWithDot + "amphithere.bounded.false");
            this.fontRenderer.drawString(bounded, this.xSize / 2 - this.fontRenderer.getStringWidth(bounded) / 2, 84, 4210752);
        }
        if (ConfigHandler.general.maleAndFemale) {
            String gender = StatCollector.translateToLocal(modIdWithDot + "amphithere.gender") + " " + StatCollector.translateToLocal(amphiData.amphiMod_master$getGender() ? modIdWithDot + "amphithere.gender.female" : modIdWithDot + "amphithere.gender.male");
            this.fontRenderer.drawString(gender, this.xSize / 2 - this.fontRenderer.getStringWidth(gender) / 2, 93, 4210752);
        }
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        drawEntityOnScreen(k + 75, l + 61, 25, (float) (k + 88) - this.mousePosX, (float) (l + 75 - 50) - this.mousePosY, this.amphithere);
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityAmphithere amphithere) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50f);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.6F, 0.6F, 0.6F);
        float f2 = amphithere.renderYawOffset;
        float f3 = amphithere.rotationYaw;
        float f4 = amphithere.rotationPitch;
        float f5 = amphithere.prevRotationYawHead;
        float f6 = amphithere.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        amphithere.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
        amphithere.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
        amphithere.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
        amphithere.rotationYawHead = amphithere.rotationYaw;
        amphithere.prevRotationYawHead = amphithere.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(amphithere, (double) 0.0F, (double) 0.0F, (double) 0.0F, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        amphithere.renderYawOffset = f2;
        amphithere.rotationYaw = f3;
        amphithere.rotationPitch = f4;
        amphithere.prevRotationYawHead = f5;
        amphithere.rotationYawHead = f6;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}