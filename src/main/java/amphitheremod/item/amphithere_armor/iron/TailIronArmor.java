package amphitheremod.item.amphithere_armor.iron;

import amphitheremod.AmphithereMod;
import amphitheremod.item.amphithere_armor.ArmorBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;

import static amphitheremod.AmphithereMod.modIdWithDot;

public class TailIronArmor extends ArmorBase {
    public TailIronArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name, CreativeTabs tab) {
        super(material, 0, slot);
        this.setRegistryName(AmphithereMod.MODID, name);
        this.setTranslationKey(modIdWithDot + name);
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.setCreativeTab(tab);
    }
}