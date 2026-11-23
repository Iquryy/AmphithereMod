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
 * A beak attachment: goes in the amphithere's main hand slot and adds attack damage. Players can't attack with it
 * (event.CommonEvents cancels the attack) since it isn't meant as a weapon. Fire, ice, lightning and silver beaks also
 * have on-hit effects (mixin.AmphiBeakMixin).
 */
public class AmphithereBeakItem extends Item {
    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

    public final AmphiMaterial material;

    public AmphithereBeakItem(AmphiMaterial material) {
        super(new Item.Properties().stacksTo(1));
        this.material = material;
    }

    /** Damage the beak adds, from the custom values or the material's sword damage times the divider. */
    public double beakDamage() {
        if (AmphiConfig.COSMETIC_ARMOR.get()) {
            return 0;
        }
        if (!AmphiConfig.CUSTOM_BEAK_DAMAGE.get()) {
            return this.material.swordAttackDamage() * AmphiConfig.BEAK_DAMAGE_DIVIDER.get();
        }
        return switch (this.material) {
            case COPPER -> AmphiConfig.COPPER_BEAK_DAMAGE.get();
            case IRON -> AmphiConfig.IRON_BEAK_DAMAGE.get();
            case GOLD -> AmphiConfig.GOLD_BEAK_DAMAGE.get();
            case DIAMOND -> AmphiConfig.DIAMOND_BEAK_DAMAGE.get();
            case SILVER -> AmphiConfig.SILVER_BEAK_DAMAGE.get();
            case DRAGONBONE -> AmphiConfig.DRAGONBONE_BEAK_DAMAGE.get();
            case FIRE -> AmphiConfig.FLAMED_BEAK_DAMAGE.get();
            case ICE -> AmphiConfig.ICED_BEAK_DAMAGE.get();
            case LIGHTNING -> AmphiConfig.SHOCKED_BEAK_DAMAGE.get();
        };
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        double damage = beakDamage();
        if (slot != EquipmentSlot.MAINHAND || damage <= 0) {
            return ImmutableMultimap.of();
        }
        return ImmutableMultimap.of(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Amphithere beak", damage, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public int getDefaultTooltipHideFlags(ItemStack stack) {
        return ItemStack.TooltipPart.MODIFIERS.getMask();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (AmphiConfig.COSMETIC_ARMOR.get()) {
            tooltip.add(Component.translatable("amphitheremod.tooltip.cosmetic").withStyle(ChatFormatting.BLUE));
            return;
        }
        tooltip.add(Component.translatable("amphitheremod.amphithere.beak_attachment")
                .append(Component.literal(" +" + new DecimalFormat("0.##").format(beakDamage()) + " ").withStyle(ChatFormatting.BLUE))
                .append(Component.translatable("amphitheremod.tooltip.attack").withStyle(ChatFormatting.BLUE)));
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
