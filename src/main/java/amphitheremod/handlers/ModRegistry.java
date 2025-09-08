package amphitheremod.handlers;

import amphitheremod.AmphithereMod;
import amphitheremod.config.ConfigHandler;
import amphitheremod.item.ItemAmphithereCrystalFeather;
import amphitheremod.item.ItemShivaxiFeather;
import amphitheremod.item.ItemXXLChocolateCookie;
import amphitheremod.item.amphithere_armor.copper.*;
import amphitheremod.item.amphithere_armor.diamond.*;
import amphitheremod.item.amphithere_armor.gold.*;
import amphitheremod.item.amphithere_armor.iron.*;
import amphitheremod.item.amphithere_armor.silver.*;
import amphitheremod.item.amphithere_beak_attachment.CopperBeak;
import amphitheremod.item.amphithere_beak_attachment.DiamondBeak;
import amphitheremod.item.amphithere_beak_attachment.GoldBeak;
import amphitheremod.item.amphithere_beak_attachment.IronBeak;
import amphitheremod.item.amphithere_beak_attachment.SilverBeak;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static amphitheremod.AmphithereMod.modIdWithDot;
import static com.github.alexthe666.iceandfire.core.ModItems.*;

@Mod.EventBusSubscriber(modid = AmphithereMod.MODID)
public class ModRegistry {

    // CREATIVE TAB
    public static final CreativeTabs AMPHITHERE_MOD_TAB = new CreativeTabs(modIdWithDot + "amphithere_mod_items") {
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(AMPHITHERE_CRYSTAL_FEATHER);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            super.displayAllRelevantItems(items);

            final List<String> materialOrder = Arrays.asList("copper", "iron", "gold", "diamond", "silver");
            final List<String> armorPartOrder = Arrays.asList("head", "wing", "body", "tail");

            items.sort(new Comparator<ItemStack>() {
                @Override
                public int compare(ItemStack stack1, ItemStack stack2) {
                    Item item1 = stack1.getItem();
                    Item item2 = stack2.getItem();
                    int category1 = getCategoryIndex(item1);
                    int category2 = getCategoryIndex(item2);
                    if (category1 != category2) return Integer.compare(category1, category2);
                    String name1 = Objects.requireNonNull(item1.getRegistryName()).getPath();
                    String name2 = Objects.requireNonNull(item2.getRegistryName()).getPath();
                    if (category1 == 0)
                        return name1.compareTo(name2);
                    else {
                        int materialIndex1 = materialOrder.indexOf(getMaterialFromItemName(name1));
                        int materialIndex2 = materialOrder.indexOf(getMaterialFromItemName(name2));
                        if (materialIndex1 != materialIndex2) return Integer.compare(materialIndex1, materialIndex2);
                        if (category1 == 2) {
                            int partIndex1 = armorPartOrder.indexOf(getArmorPartFromItemName(name1));
                            int partIndex2 = armorPartOrder.indexOf(getArmorPartFromItemName(name2));
                            return Integer.compare(partIndex1, partIndex2);
                        }
                        return 0;
                    }
                }

                private int getCategoryIndex(Item item) {
                    String registryName = Objects.requireNonNull(item.getRegistryName()).getPath();
                    if (registryName.contains("beak_attachment")) return 1;
                    if (item instanceof ItemArmor) return 2;
                    return 0;
                }

                private String getMaterialFromItemName(String name) {
                    if (name.contains("copper")) return "copper";
                    if (name.contains("iron")) return "iron";
                    if (name.contains("gold")) return "gold";
                    if (name.contains("diamond")) return "diamond";
                    if (name.contains("silver")) return "silver";
                    return "";
                }

                private String getArmorPartFromItemName(String name) {
                    if (name.contains("head")) return "head";
                    if (name.contains("wing")) return "wing";
                    if (name.contains("body")) return "body";
                    if (name.contains("tail")) return "tail";
                    return "";
                }
            });
        }
    };

    // ITEMS
    public static List<Item> itemsToRegister = new ArrayList<>();
    public static Item SHIVAXI_FEATHER;
    public static Item XXL_CHOCOLATE_COOKIE;
    public static Item AMPHITHERE_CRYSTAL_FEATHER;
    public static Item AMPHITHERE_COPPER_HEAD_ARMOR;
    public static Item AMPHITHERE_COPPER_WING_ARMOR;
    public static Item AMPHITHERE_COPPER_BODY_ARMOR;
    public static Item AMPHITHERE_COPPER_TAIL_ARMOR;
    public static Item AMPHITHERE_COPPER_BEAK_ATTACHMENT;
    public static Item AMPHITHERE_IRON_HEAD_ARMOR;
    public static Item AMPHITHERE_IRON_WING_ARMOR;
    public static Item AMPHITHERE_IRON_BODY_ARMOR;
    public static Item AMPHITHERE_IRON_TAIL_ARMOR;
    public static Item AMPHITHERE_IRON_BEAK_ATTACHMENT;
    public static Item AMPHITHERE_GOLD_HEAD_ARMOR;
    public static Item AMPHITHERE_GOLD_WING_ARMOR;
    public static Item AMPHITHERE_GOLD_BODY_ARMOR;
    public static Item AMPHITHERE_GOLD_TAIL_ARMOR;
    public static Item AMPHITHERE_GOLD_BEAK_ATTACHMENT;
    public static Item AMPHITHERE_DIAMOND_HEAD_ARMOR;
    public static Item AMPHITHERE_DIAMOND_WING_ARMOR;
    public static Item AMPHITHERE_DIAMOND_TAIL_ARMOR;
    public static Item AMPHITHERE_DIAMOND_BODY_ARMOR;
    public static Item AMPHITHERE_DIAMOND_BEAK_ATTACHMENT;
    public static Item AMPHITHERE_SILVER_HEAD_ARMOR;
    public static Item AMPHITHERE_SILVER_WING_ARMOR;
    public static Item AMPHITHERE_SILVER_TAIL_ARMOR;
    public static Item AMPHITHERE_SILVER_BODY_ARMOR;
    public static Item AMPHITHERE_SILVER_BEAK_ATTACHMENT;

    @SubscribeEvent
    public static void registerItemEvent(RegistryEvent.Register<Item> event) {
        itemsToRegister.add(SHIVAXI_FEATHER = new ItemShivaxiFeather("shivaxi_feather", AMPHITHERE_MOD_TAB));
        itemsToRegister.add(XXL_CHOCOLATE_COOKIE = new ItemXXLChocolateCookie("xxl_chocolate_cookie", AMPHITHERE_MOD_TAB));
        itemsToRegister.add(AMPHITHERE_CRYSTAL_FEATHER = new ItemAmphithereCrystalFeather("amphithere_crystal_feather", AMPHITHERE_MOD_TAB));

        if (ConfigHandler.general.enableAmphithereArmor) {
            itemsToRegister.add(AMPHITHERE_COPPER_HEAD_ARMOR = new HeadCopperArmor(copperMetal, EntityEquipmentSlot.HEAD, "copper_head_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_COPPER_WING_ARMOR = new WingCopperArmor(copperMetal, EntityEquipmentSlot.LEGS, "copper_wing_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_COPPER_BODY_ARMOR = new BodyCopperArmor(copperMetal, EntityEquipmentSlot.CHEST, "copper_body_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_COPPER_TAIL_ARMOR = new TailCopperArmor(copperMetal, EntityEquipmentSlot.FEET, "copper_tail_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_COPPER_BEAK_ATTACHMENT = new CopperBeak(copperTools, "copper_beak_attachment", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_IRON_HEAD_ARMOR = new HeadIronArmor(ItemArmor.ArmorMaterial.IRON, EntityEquipmentSlot.HEAD, "iron_head_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_IRON_WING_ARMOR = new WingIronArmor(ItemArmor.ArmorMaterial.IRON, EntityEquipmentSlot.LEGS, "iron_wing_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_IRON_BODY_ARMOR = new BodyIronArmor(ItemArmor.ArmorMaterial.IRON, EntityEquipmentSlot.CHEST, "iron_body_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_IRON_TAIL_ARMOR = new TailIronArmor(ItemArmor.ArmorMaterial.IRON, EntityEquipmentSlot.FEET, "iron_tail_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_IRON_BEAK_ATTACHMENT = new IronBeak(Item.ToolMaterial.IRON, "iron_beak_attachment", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_GOLD_HEAD_ARMOR = new HeadGoldArmor(ItemArmor.ArmorMaterial.GOLD, EntityEquipmentSlot.HEAD, "gold_head_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_GOLD_WING_ARMOR = new WingGoldArmor(ItemArmor.ArmorMaterial.GOLD, EntityEquipmentSlot.LEGS, "gold_wing_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_GOLD_BODY_ARMOR = new BodyGoldArmor(ItemArmor.ArmorMaterial.GOLD, EntityEquipmentSlot.CHEST, "gold_body_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_GOLD_TAIL_ARMOR = new TailGoldArmor(ItemArmor.ArmorMaterial.GOLD, EntityEquipmentSlot.FEET, "gold_tail_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_GOLD_BEAK_ATTACHMENT = new GoldBeak(Item.ToolMaterial.GOLD, "gold_beak_attachment", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_DIAMOND_HEAD_ARMOR = new HeadDiamondArmor(ItemArmor.ArmorMaterial.DIAMOND, EntityEquipmentSlot.HEAD, "diamond_head_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_DIAMOND_WING_ARMOR = new WingDiamondArmor(ItemArmor.ArmorMaterial.DIAMOND, EntityEquipmentSlot.LEGS, "diamond_wing_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_DIAMOND_TAIL_ARMOR = new TailDiamondArmor(ItemArmor.ArmorMaterial.DIAMOND, EntityEquipmentSlot.FEET, "diamond_tail_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_DIAMOND_BODY_ARMOR = new BodyDiamondArmor(ItemArmor.ArmorMaterial.DIAMOND, EntityEquipmentSlot.CHEST, "diamond_body_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_DIAMOND_BEAK_ATTACHMENT = new DiamondBeak(Item.ToolMaterial.DIAMOND, "diamond_beak_attachment", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_SILVER_HEAD_ARMOR = new HeadSilverArmor(silverMetal, EntityEquipmentSlot.HEAD, "silver_head_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_SILVER_WING_ARMOR = new WingSilverArmor(silverMetal, EntityEquipmentSlot.LEGS, "silver_wing_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_SILVER_TAIL_ARMOR = new TailSilverArmor(silverMetal, EntityEquipmentSlot.FEET, "silver_tail_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_SILVER_BODY_ARMOR = new BodySilverArmor(silverMetal, EntityEquipmentSlot.CHEST, "silver_body_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_SILVER_BEAK_ATTACHMENT = new SilverBeak(silverTools, "silver_beak_attachment", AMPHITHERE_MOD_TAB));
        }
        if (!itemsToRegister.isEmpty())
            event.getRegistry().registerAll(itemsToRegister.toArray(new Item[0]));
    }

    public static void init() {
    }
}