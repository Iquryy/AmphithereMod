package amphitheremod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * XXL Chocolate Cookie and Bloody Cookie. Players can eat them (same food values as 1.12.2); fed to an amphithere they
 * do their special thing (mixin.AmphiInteractMixin).
 */
public class ItemAmphiCookie extends Item {
    private final String tooltipKey;

    public ItemAmphiCookie(String tooltipKey) {
        super(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(6.0F).build()));
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(this.tooltipKey));
    }
}
