package amphitheremod.registry;

import amphitheremod.AmphithereMod;
import amphitheremod.config.AmphiConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/** The "Amphithere Mod" tab, in 1.12.2's order: misc items, eggs by rarity, beaks, then armor by material and part. */
public final class AmphiCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AmphithereMod.MODID);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("amphithere_mod_items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.amphitheremod.amphithere_mod_items"))
            .icon(() -> new ItemStack(AmphiItems.SHIVAXI_FEATHER.get()))
            .displayItems((params, output) -> {
                // Disabled features are left out, the same way 1.12.2 didn't register them
                if (AmphiConfig.ENABLE_CRYSTAL_FEATHER.get()) {
                    output.accept(AmphiItems.CRYSTAL_FEATHER.get());
                }
                if (AmphiConfig.ENABLE_ANTI_COTH_COOKIE.get() && ModList.get().isLoaded("epca")) {
                    output.accept(AmphiItems.ANTI_COTH_COOKIE.get());
                }
                output.accept(AmphiItems.SHIVAXI_FEATHER.get());
                if (AmphiConfig.ENABLE_XXL_COOKIE.get()) {
                    output.accept(AmphiItems.XXL_CHOCOLATE_COOKIE.get());
                }
                if (AmphiConfig.ENABLE_EGGS.get()) {
                    AmphiItems.eggsByRarity().forEach(output::accept);
                }
                if (AmphiConfig.ENABLE_ARMOR.get()) {
                    AmphiItems.BEAKS.forEach(beak -> output.accept(beak.get()));
                    AmphiItems.ARMOR.forEach(armor -> output.accept(armor.get()));
                }
            })
            .build());

    private AmphiCreativeTab() {
    }
}
