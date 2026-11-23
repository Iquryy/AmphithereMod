package amphitheremod.item;

import amphitheremod.config.AmphiConfig;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * One piece of amphithere armor. A plain Item (not ArmorItem) so players can't wear it and armor mods (FirstAid,
 * quality reforging) leave it alone; it only does anything in an amphithere's equipment slot, where vanilla applies
 * the armor modifier below. Slots: HEAD = head, CHEST = body, LEGS = wings, FEET = tail (same as 1.12.2).
 */
public class AmphithereArmorItem extends Item {
    // Same UUIDs vanilla uses per armor slot, so a piece replaces rather than stacks with another in that slot
    private static final UUID[] ARMOR_MODIFIERS = {
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    public final AmphiMaterial material;
    public final EquipmentSlot slot;

    public AmphithereArmorItem(AmphiMaterial material, EquipmentSlot slot) {
        super(new Item.Properties().stacksTo(1));
        this.material = material;
        this.slot = slot;
    }

    /** Armor points this piece gives after the divider config, or 0 when armor is cosmetic. */
    public double armorPoints() {
        if (AmphiConfig.COSMETIC_ARMOR.get()) {
            return 0;
        }
        return this.material.armorFor(this.slot) * AmphiConfig.ARMOR_DIVIDER.get();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipSlot, ItemStack stack) {
        double armor = armorPoints();
        if (equipSlot != this.slot || armor <= 0) {
            return ImmutableMultimap.of();
        }
        return ImmutableMultimap.of(Attributes.ARMOR,
                new AttributeModifier(ARMOR_MODIFIERS[this.slot.getIndex()], "Amphithere armor", armor, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public int getDefaultTooltipHideFlags(ItemStack stack) {
        // Hide vanilla's "When on Head: +1 Armor" lines; the tooltip below names the amphithere part instead
        return ItemStack.TooltipPart.MODIFIERS.getMask();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (AmphiConfig.COSMETIC_ARMOR.get()) {
            tooltip.add(Component.translatable("amphitheremod.tooltip.cosmetic").withStyle(ChatFormatting.BLUE));
            return;
        }
        String part = switch (this.slot) {
            case HEAD -> "armor_head";
            case LEGS -> "armor_wings";
            case CHEST -> "armor_body";
            default -> "armor_tail";
        };
        tooltip.add(Component.translatable("amphitheremod.amphithere." + part)
                .append(Component.literal(" +" + new DecimalFormat("0.##").format(armorPoints()) + " ").withStyle(ChatFormatting.BLUE))
                .append(Component.translatable("amphitheremod.tooltip.armor").withStyle(ChatFormatting.BLUE)));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
