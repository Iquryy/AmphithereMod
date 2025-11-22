package amphitheremod.item.amphithere_armor;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.UsefulStiff;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static amphitheremod.AmphithereMod.modIdWithDot;

public class ArmorBase extends ItemArmor {
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    ArmorMaterial material;
    EntityEquipmentSlot equipSlot;
    public ArmorBase(ArmorMaterial mat, int i, EntityEquipmentSlot slot) {
        super(mat, i, slot);
        this.equipSlot = slot;
        this.material = mat;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (ConfigHandler.amphithereArmor.cosmeticArmorBeak) {
            tooltip.add(UsefulStiff.translateToLocal(TextFormatting.BLUE + "Cosmetic"));
        } else {
            Multimap<String, AttributeModifier> attributeModifiers = stack.getAttributeModifiers(this.armorType);
            Collection<AttributeModifier> armorModifiers = attributeModifiers.get(SharedMonsterAttributes.ARMOR.getName());
            double totalArmor = 0;
            double flySpeed = 0;

            if (armorModifiers != null && !armorModifiers.isEmpty()) {
                for (AttributeModifier modifier : armorModifiers) {
                    if (modifier.getOperation() == 0) {
                        totalArmor += modifier.getAmount();
                        flySpeed -= totalArmor / 20;
                    }
                }
                DecimalFormat df = new DecimalFormat("0.##");
                switch (equipSlot) {
                    case HEAD:
                        tooltip.add(UsefulStiff.translateToLocal(modIdWithDot + "amphithere.armor_head") + TextFormatting.BLUE + " +" + df.format(totalArmor) + " " + UsefulStiff.translateToLocal(modIdWithDot + "tooltip.armor") + TextFormatting.RESET);
                        //tooltip.add(StatCollector.translateToLocal(modIdWithDot + "tooltip.flightspeed") + TextFormatting.BLUE + " " + df.format(flySpeed) + " " + TextFormatting.RESET);
                        break;
                    case LEGS:
                        tooltip.add(UsefulStiff.translateToLocal(modIdWithDot + "amphithere.armor_wings") + TextFormatting.BLUE + " +" + df.format(totalArmor) + " " + UsefulStiff.translateToLocal(modIdWithDot + "tooltip.armor") + TextFormatting.RESET);
                        //tooltip.add(StatCollector.translateToLocal(modIdWithDot + "tooltip.flightspeed") + TextFormatting.BLUE + " " + df.format(flySpeed) + " " + TextFormatting.RESET);
                        break;
                    case CHEST:
                        tooltip.add(UsefulStiff.translateToLocal(modIdWithDot + "amphithere.armor_body") + TextFormatting.BLUE + " +" + df.format(totalArmor) + " " + UsefulStiff.translateToLocal(modIdWithDot + "tooltip.armor") + TextFormatting.RESET);
                        //tooltip.add(StatCollector.translateToLocal(modIdWithDot + "tooltip.flightspeed") + TextFormatting.BLUE + " " + df.format(flySpeed) + " " + TextFormatting.RESET);
                        break;
                    case FEET:
                        tooltip.add(UsefulStiff.translateToLocal(modIdWithDot + "amphithere.armor_tail") + TextFormatting.BLUE + " +" + df.format(totalArmor) + " " + UsefulStiff.translateToLocal(modIdWithDot + "tooltip.armor") + TextFormatting.RESET);
                        //tooltip.add(StatCollector.translateToLocal(modIdWithDot + "tooltip.flightspeed") + TextFormatting.BLUE + " " + df.format(flySpeed) + " " + TextFormatting.RESET);
                        break;
                }
            }
        }
    }

    @NotNull
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(@NotNull EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
        multimap.removeAll(SharedMonsterAttributes.ARMOR.getName());
        multimap.removeAll(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName());

        if (!ConfigHandler.amphithereArmor.cosmeticArmorBeak && slot == this.armorType) {
            double defaultArmorPoints = this.material.getDamageReductionAmount(slot);
            double modifiedArmorPoints = defaultArmorPoints * ConfigHandler.amphithereArmor.armorPointDivider;
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", modifiedArmorPoints, 0));
        }

        return multimap;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setBoolean("Unbreakable", true);
        stack.getTagCompound().setInteger("HideFlags", 6);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            if (stack.getTagCompound() == null) {
                stack.setTagCompound(new NBTTagCompound());
            }
            stack.getTagCompound().setBoolean("Unbreakable", true);
            stack.getTagCompound().setInteger("HideFlags", 6);
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
