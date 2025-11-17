package amphitheremod.client.layer;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static amphitheremod.util.EnumAmphiType.*;
import static amphitheremod.util.Refs.*;

@SideOnly(Side.CLIENT)
public class LayerAmphithereHeadless extends AbstractAmphithereLayer {
    public LayerAmphithereHeadless(RenderLiving<EntityAmphithere> rendererIn) {
        super(rendererIn);
    }

    @Override
    protected ResourceLocation getTextureToBind(EntityAmphithere amphi) {
        String amphiName = amphi.getName().toLowerCase();
        if(amphiName.isEmpty()) return EMPTY;
        int amphiVariant = amphi.getVariant();
        if (amphiVariant == SKELETON.ordinal() || amphiVariant == WITHER_SKELETON.ordinal())
            return EMPTY;
        if (amphiName.equals("h-2"))
            return HEADLESS;
        else
            return EMPTY;
    }
}