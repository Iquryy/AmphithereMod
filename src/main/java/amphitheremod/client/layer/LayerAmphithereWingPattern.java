package amphitheremod.client.layer;

import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static amphitheremod.util.Refs.*;

@SideOnly(Side.CLIENT)
public class LayerAmphithereWingPattern extends AbstractAmphithereLayer {
    public LayerAmphithereWingPattern(RenderLiving<EntityAmphithere> rendererIn) {
        super(rendererIn);
    }

    @Override protected ResourceLocation getTextureToBind(EntityAmphithere amphithere) {
        if (!(amphithere instanceof IAmphithereData)) return EMPTY;
        IAmphithereData data = (IAmphithereData) amphithere;
        String wingPattern = data.amphiMod_master$getWingPattern();
        switch (wingPattern){
            case "AQUA":
                return AQUA;
            case "BLUE":
                return BLUE;
            case "CYAN":
                return CYAN;
            case "GREEN":
                return GREEN;
            case "DARK_PURPLE":
                return DARK_PURPLE;
            case "PURPLE":
                return PURPLE;
            case "ORANGE":
                return ORANGE;
            case "RED":
                return RED;
            case "TEAL":
                return TEAL;
            case "BLACK":
                return BLACK;
            case "WHITE":
                return WHITE;
            default:
                return EMPTY;
        }
    }
}

