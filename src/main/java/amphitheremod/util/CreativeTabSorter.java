package amphitheremod.util;

import amphitheremod.item.ItemAmphithereEgg;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CreativeTabSorter {
    private static final List<String> MATERIAL_ORDER = Arrays.asList("copper", "iron", "gold", "diamond", "silver");
    private static final List<String> ARMOR_PART_ORDER = Arrays.asList("head", "wing", "body", "tail");

    public static int compareItems(ItemStack stack1, ItemStack stack2) {
        Item item1 = stack1.getItem();
        Item item2 = stack2.getItem();

        int category1 = getCategoryIndex(item1);
        int category2 = getCategoryIndex(item2);

        if (category1 != category2) {
            return Integer.compare(category1, category2);
        }

        switch (category1) {
            case 1: // Eggs
                return compareEggRarity(stack1, stack2);
            case 2: // Beak Attachments
            case 3: // Armor
                return compareArmor(item1, item2);
            default: // Other items (category 0)
                return Objects.requireNonNull(item1.getRegistryName()).getPath()
                        .compareTo(Objects.requireNonNull(item2.getRegistryName()).getPath());
        }
    }

    private static int getCategoryIndex(Item item) {
        if (item instanceof ItemAmphithereEgg) return 1;
        String registryName = Objects.requireNonNull(item.getRegistryName()).getPath();
        if (registryName.contains("beak_attachment")) return 2;
        if (item instanceof ItemArmor) return 3;
        return 0;
    }

    private static int compareEggRarity(ItemStack stack1, ItemStack stack2) {
        EnumRarity rarity1 = stack1.getRarity();
        EnumRarity rarity2 = stack2.getRarity();
        return Integer.compare(rarityToValue(rarity1), rarityToValue(rarity2));
    }

    private static int rarityToValue(EnumRarity rarity) {
        if (rarity == EnumRarity.COMMON) return 0;
        if (rarity == EnumRarity.UNCOMMON) return 1;
        if (rarity == EnumRarity.RARE) return 2;
        if (rarity == EnumRarity.EPIC) return 3;
        return 4;
    }

    private static int compareArmor(Item item1, Item item2) {
        String name1 = Objects.requireNonNull(item1.getRegistryName()).getPath();
        String name2 = Objects.requireNonNull(item2.getRegistryName()).getPath();

        int materialIndex1 = MATERIAL_ORDER.indexOf(getMaterialFromItemName(name1));
        int materialIndex2 = MATERIAL_ORDER.indexOf(getMaterialFromItemName(name2));

        if (materialIndex1 != materialIndex2) {
            return Integer.compare(materialIndex1, materialIndex2);
        }

        if (item1 instanceof ItemArmor) {
            int partIndex1 = ARMOR_PART_ORDER.indexOf(getArmorPartFromItemName(name1));
            int partIndex2 = ARMOR_PART_ORDER.indexOf(getArmorPartFromItemName(name2));
            return Integer.compare(partIndex1, partIndex2);
        }

        return 0;
    }

    private static String getMaterialFromItemName(String name) {
        for (String material : MATERIAL_ORDER) {
            if (name.contains(material)) {
                return material;
            }
        }
        return "";
    }

    private static String getArmorPartFromItemName(String name) {
        for (String part : ARMOR_PART_ORDER) {
            if (name.contains(part)) {
                return part;
            }
        }
        return "";
    }
}
