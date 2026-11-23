package amphitheremod.registry;

import amphitheremod.AmphithereMod;
import amphitheremod.menu.AmphithereMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class AmphiMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, AmphithereMod.MODID);

    // The open packet carries the amphithere's entity id so the client can build the same menu
    public static final RegistryObject<MenuType<AmphithereMenu>> AMPHITHERE = MENUS.register("amphithere",
            () -> IForgeMenuType.create((id, inv, buf) -> AmphithereMenu.fromNetwork(id, inv, buf.readVarInt())));

    private AmphiMenus() {
    }
}
