package amphitheremod.item;

import amphitheremod.AmphithereMod;
import amphitheremod.util.UsefulStiff;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static amphitheremod.AmphithereMod.modIdWithDot;
import static amphitheremod.handlers.ModItemRegistry.itemsToRegister;

public class ItemAntiCothCookie extends ItemFood {
    public ItemAntiCothCookie(String name, CreativeTabs tab) {
        super(6, 6, false);
        this.setRegistryName(AmphithereMod.MODID, name);
        this.setTranslationKey(modIdWithDot + name);
        this.setCreativeTab(tab);
        itemsToRegister.add(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(UsefulStiff.translateToLocal(modIdWithDot+"tooltip.coth.cookie"));
    }
}
