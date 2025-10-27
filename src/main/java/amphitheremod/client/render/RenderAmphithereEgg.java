package amphitheremod.client.render;

import amphitheremod.client.model.ModelAmphithereEgg;
import amphitheremod.entity.EntityAmphithereEgg;
import amphitheremod.util.enumm.EnumAmphiType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAmphithereEgg extends RenderLiving<EntityAmphithereEgg> {

    public RenderAmphithereEgg(RenderManager renderManager) {
        super(renderManager, new ModelAmphithereEgg(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityAmphithereEgg entity) {
        EnumAmphiType variant = entity.getType();
        String resLoc = variant.getTexturePath();
        return new ResourceLocation("amphitheremod:textures/entity/amphithere_egg/"+resLoc+".png");
    }

    @Override
    protected void preRenderCallback(EntityAmphithereEgg entityliving, float f) {
        float scale = 0.6F;
        GlStateManager.scale(scale, scale, scale);
        this.shadowSize = 0.15F;
    }
}