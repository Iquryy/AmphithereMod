package amphitheremod.client.layer;

import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static amphitheremod.util.EnumAmphiType.SKELETON;
import static amphitheremod.util.EnumAmphiType.WITHER_SKELETON;
import static amphitheremod.util.Refs.*;

@SideOnly(Side.CLIENT)
public class LayerAmphithereGender extends AbstractAmphithereLayer {
    public LayerAmphithereGender(RenderLiving<EntityAmphithere> rendererIn) {
        super(rendererIn);
    }

    @Override protected ResourceLocation getTextureToBind(EntityAmphithere amphithere) {
        if (!(amphithere instanceof IAmphithereData)) return EMPTY;

        IAmphithereData data = (IAmphithereData) amphithere;
        int amphiVariant = amphithere.getVariant();
        if (amphiVariant == SKELETON.ordinal() || amphiVariant == WITHER_SKELETON.ordinal())
            return EMPTY;
        else
            return data.amphiMod_master$getGender() ? FEMALE : MALE;
    }
}
