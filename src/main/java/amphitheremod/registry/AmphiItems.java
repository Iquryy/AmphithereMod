package amphitheremod.registry;

import amphitheremod.AmphithereMod;
import amphitheremod.item.AmphiMaterial;
import amphitheremod.item.AmphithereArmorItem;
import amphitheremod.item.AmphithereBeakItem;
import amphitheremod.item.ItemAmphiCookie;
import amphitheremod.item.ItemAmphithereEgg;
import amphitheremod.item.ItemCrystalFeather;
import amphitheremod.item.ItemShivaxiFeather;
import amphitheremod.util.EnumAmphiType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Every item, with the same registry names as 1.12.2. Items are always registered (a client and server with different
 * configs would otherwise disagree on the item registry); config switches hide them from the creative tab, disable
 * their recipes and turn off what they do instead.
 */
public final class AmphiItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AmphithereMod.MODID);

    public static final RegistryObject<Item> SHIVAXI_FEATHER = ITEMS.register("shivaxi_feather", ItemShivaxiFeather::new);
    public static final RegistryObject<Item> CRYSTAL_FEATHER = ITEMS.register("amphithere_crystal_feather", ItemCrystalFeather::new);
    public static final RegistryObject<Item> XXL_CHOCOLATE_COOKIE = ITEMS.register("xxl_chocolate_cookie",
            () -> new ItemAmphiCookie("amphitheremod.tooltip.xxl.cookie"));
    public static final RegistryObject<Item> ANTI_COTH_COOKIE = ITEMS.register("anti_coth_cookie",
            () -> new ItemAmphiCookie("amphitheremod.tooltip.coth.cookie"));

    /** Armor in creative-tab order: per material, head, wing, body, tail. */
    public static final List<RegistryObject<Item>> ARMOR = new ArrayList<>();
    /** Beaks in creative-tab order. */
    public static final List<RegistryObject<Item>> BEAKS = new ArrayList<>();
    private static final Map<EnumAmphiType, RegistryObject<Item>> EGGS = new EnumMap<>(EnumAmphiType.class);

    static {
        for (AmphiMaterial material : new AmphiMaterial[]{AmphiMaterial.COPPER, AmphiMaterial.IRON, AmphiMaterial.GOLD, AmphiMaterial.DIAMOND, AmphiMaterial.SILVER}) {
            ARMOR.add(armor(material, "head", EquipmentSlot.HEAD));
            ARMOR.add(armor(material, "wing", EquipmentSlot.LEGS));
            ARMOR.add(armor(material, "body", EquipmentSlot.CHEST));
            ARMOR.add(armor(material, "tail", EquipmentSlot.FEET));
        }
        for (AmphiMaterial material : AmphiMaterial.values()) {
            BEAKS.add(ITEMS.register(material.name + "_beak_attachment", () -> new AmphithereBeakItem(material)));
        }
        for (EnumAmphiType type : EnumAmphiType.values()) {
            EGGS.put(type, ITEMS.register("amphithere_" + type.name().toLowerCase() + "_egg", () -> new ItemAmphithereEgg(type)));
        }
    }

    private AmphiItems() {
    }

    private static RegistryObject<Item> armor(AmphiMaterial material, String part, EquipmentSlot slot) {
        return ITEMS.register(material.name + "_" + part + "_armor", () -> new AmphithereArmorItem(material, slot));
    }

    /** Egg item for a variant. */
    @Nullable
    public static Item eggFor(EnumAmphiType type) {
        RegistryObject<Item> egg = EGGS.get(type);
        return egg == null ? null : egg.get();
    }

    /** Eggs in rarity order (common first), for the creative tab. */
    public static List<Item> eggsByRarity() {
        List<Item> list = new ArrayList<>();
        for (EnumAmphiType.Group group : EnumAmphiType.Group.values()) {
            for (EnumAmphiType type : EnumAmphiType.values()) {
                if (type.getGroup() == group) {
                    list.add(EGGS.get(type).get());
                }
            }
        }
        return list;
    }

    /** The beak item in an amphithere's beak slot that matches the given material, if any. */
    public static boolean isBeak(Item item, AmphiMaterial material) {
        return item instanceof AmphithereBeakItem beak && beak.material == material;
    }
}
