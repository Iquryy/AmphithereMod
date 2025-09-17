package amphitheremod.item.amphithere_armor;

import amphitheremod.util.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

import static amphitheremod.AmphithereMod.modIdWithDot;

public class ArmorBase extends ItemArmor {
    EntityEquipmentSlot equipSlot;
    public ArmorBase(ArmorMaterial mat, int i, EntityEquipmentSlot slot) {
        super(mat, i, slot);
        this.equipSlot = slot;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        Multimap<String, AttributeModifier> attributeModifiers = stack.getAttributeModifiers(this.armorType);
        Collection<AttributeModifier> armorModifiers = attributeModifiers.get(SharedMonsterAttributes.ARMOR.getName());
        double totalArmor = 0;
        if (armorModifiers != null && !armorModifiers.isEmpty()) {
            for (AttributeModifier modifier : armorModifiers) {
                if (modifier.getOperation() == 0) {
                    totalArmor += modifier.getAmount();
                }
            }
            DecimalFormat df = new DecimalFormat("0.##");
            switch (equipSlot) {
                case HEAD:
                    tooltip.add(StatCollector.translateToLocal(modIdWithDot + "amphithere.armor_head") + TextFormatting.BLUE + " +" + df.format(totalArmor) + " " + StatCollector.translateToLocal(modIdWithDot + "tooltip.armor") + TextFormatting.RESET);
                    break;
                case LEGS:
                    tooltip.add(StatCollector.translateToLocal(modIdWithDot + "amphithere.armor_wings") + TextFormatting.BLUE + " +" + df.format(totalArmor) + " " + StatCollector.translateToLocal(modIdWithDot + "tooltip.armor") + TextFormatting.RESET);
                    break;
                case CHEST:
                    tooltip.add(StatCollector.translateToLocal(modIdWithDot + "amphithere.armor_body") + TextFormatting.BLUE + " +" + df.format(totalArmor) + " " + StatCollector.translateToLocal(modIdWithDot + "tooltip.armor") + TextFormatting.RESET);
                    break;
                case FEET:
                    tooltip.add(StatCollector.translateToLocal(modIdWithDot + "amphithere.armor_tail") + TextFormatting.BLUE + " +" + df.format(totalArmor) + " " + StatCollector.translateToLocal(modIdWithDot + "tooltip.armor") + TextFormatting.RESET);
                    break;
            }
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
    public boolean isValidArmor(@Nonnull ItemStack stack, @Nonnull EntityEquipmentSlot slot, @Nonnull Entity entity) {
        return entity instanceof EntityAmphithere && (slot == equipSlot);
    }

    @Override
    public boolean isEnchantable(ItemStack item) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack1, ItemStack stack2) {
        return false;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stackInHand = playerIn.getHeldItem(handIn);
        return new ActionResult<>(EnumActionResult.FAIL, stackInHand);
    }

    @Override
    @Nullable
    public EntityEquipmentSlot getEquipmentSlot(@Nonnull ItemStack stack) {
        return EntityEquipmentSlot.MAINHAND; //Mainhand for zombies to pick it up but not wear it as armor (EntityLiving.updateEquipmentIfNeeded), default slot for all items
    }
}
