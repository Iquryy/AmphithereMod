package amphitheremod.item.amphithere_beak_attachment;

import amphitheremod.util.StatCollector;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

import static amphitheremod.AmphithereMod.modIdWithDot;

public class BeakBase extends ItemSword {
    public BeakBase(ToolMaterial mat) {
        super(mat);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        Multimap<String, AttributeModifier> attributeModifiers = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        Collection<AttributeModifier> weaponModifier = attributeModifiers.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
        double totalDamage = 1;
        if (weaponModifier != null && !weaponModifier.isEmpty()) {
            for (AttributeModifier modifier : weaponModifier) {
                if (modifier.getOperation() == 0) {
                    totalDamage += modifier.getAmount();
                }
            }
            DecimalFormat df = new DecimalFormat("0.##");
            tooltip.add(StatCollector.translateToLocal(modIdWithDot + "amphithere.beak_attachment") + TextFormatting.BLUE + " +" + df.format(totalDamage-1) + " " + StatCollector.translateToLocal(modIdWithDot + "tooltip.attack") + TextFormatting.RESET);
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        stack.setTagInfo("HideFlags", new NBTTagInt(2));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            stack.setTagInfo("HideFlags", new NBTTagInt(2));
            items.add(stack);
        }
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack item) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack1, ItemStack stack2) {
        return false;
    }
}
