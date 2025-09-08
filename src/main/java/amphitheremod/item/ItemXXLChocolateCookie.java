package amphitheremod.item;

import amphitheremod.AmphithereMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

import static amphitheremod.AmphithereMod.modIdWithDot;

public class ItemXXLChocolateCookie extends ItemFood {
    public ItemXXLChocolateCookie(String name, CreativeTabs tab) {
        super(6, 6, false);
        this.setRegistryName(AmphithereMod.MODID, name);
        this.setTranslationKey(modIdWithDot + name);
        this.setCreativeTab(tab);
    }
}
