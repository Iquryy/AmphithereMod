package amphitheremod.item;

import amphitheremod.AmphithereMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static amphitheremod.AmphithereMod.modIdWithDot;
import static amphitheremod.handlers.ModItemRegistry.itemsToRegister;

public class ItemShivaxiFeather extends Item{
    public ItemShivaxiFeather(String name, CreativeTabs tab){
        this.setRegistryName(AmphithereMod.MODID, name);
        this.setTranslationKey(modIdWithDot + name);
        this.setCreativeTab(tab);
        itemsToRegister.add(this);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}