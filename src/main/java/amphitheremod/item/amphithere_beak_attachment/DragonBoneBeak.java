package amphitheremod.item.amphithere_beak_attachment;

import amphitheremod.AmphithereMod;
import net.minecraft.creativetab.CreativeTabs;

import static amphitheremod.AmphithereMod.modIdWithDot;

public class DragonBoneBeak extends BeakBase {
    public DragonBoneBeak(ToolMaterial material, String name, CreativeTabs tab) {
        super(material);
        this.setRegistryName(AmphithereMod.MODID, name);
        this.setTranslationKey(modIdWithDot + name);
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.setCreativeTab(tab);
    }
}
