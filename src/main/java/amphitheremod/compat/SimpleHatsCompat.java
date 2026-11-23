package amphitheremod.compat;

import amphitheremod.AmphithereMod;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

/**
 * SimpleHats support (takes the place of 1.12.2's ClassyHats support) without a compile-time dependency. An amphithere
 * wears a hat in its off-hand slot, like 1.12.2 did; only SimpleHats' HatItem counts, so its bags and scraps don't.
 */
public final class SimpleHatsCompat {
    private static final String HAT_ITEM = "fonnymunkey.simplehats.common.item.HatItem";
    private static boolean looked;
    @Nullable
    private static Class<?> hatItem;

    private SimpleHatsCompat() {
    }

    /** True when SimpleHats is installed and its hat class was found. */
    public static boolean isLoaded() {
        if (!looked) {
            looked = true;
            if (ModList.get().isLoaded("simplehats")) {
                try {
                    hatItem = Class.forName(HAT_ITEM);
                } catch (ClassNotFoundException e) {
                    AmphithereMod.LOGGER.warn("SimpleHats found but {} is missing; amphitheres can't wear hats", HAT_ITEM);
                }
            }
        }
        return hatItem != null;
    }

    /** True for a SimpleHats hat. */
    public static boolean isHat(ItemStack stack) {
        return !stack.isEmpty() && isLoaded() && hatItem.isInstance(stack.getItem());
    }
}
