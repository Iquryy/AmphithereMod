package amphitheremod.client.layer;

import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerAmphithereClassyHat implements LayerRenderer<EntityAmphithere> {

    protected final RenderLiving<EntityAmphithere> renderer;

    public LayerAmphithereClassyHat(RenderLiving<EntityAmphithere> rendererIn) {
        this.renderer = rendererIn;
    }

    @Override
    public void doRenderLayer(EntityAmphithere amphithere, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        if (Loader.isModLoaded("classyhats")) {
            ItemStack hatStack = amphithere.getHeldItem(EnumHand.OFF_HAND);
            if (hatStack.isEmpty() || !hatStack.getItem().getRegistryName().getNamespace().equals("classyhats")) return;
            GlStateManager.pushMatrix();
            ModelAmphithere model = (ModelAmphithere) this.renderer.getMainModel();

            if (amphithere.isChild()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }

            model.BodyUpper.postRender(scale);
            model.Neck1.postRender(scale);
            model.Neck2.postRender(scale);
            model.Neck3.postRender(scale);
            model.Head.postRender(scale);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.07F, -0.004F);
            GlStateManager.scale(0.31F, 0.31F, 0.31F);
            Minecraft.getMinecraft().getRenderItem().renderItem(hatStack, ItemCameraTransforms.TransformType.HEAD);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}