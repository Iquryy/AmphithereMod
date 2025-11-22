package amphitheremod.handlers;

import amphitheremod.AmphithereMod;
import amphitheremod.config.ConfigHandler;
import amphitheremod.item.*;
import amphitheremod.item.ItemXXLChocolateCookie;
import amphitheremod.item.amphithere_armor.copper.*;
import amphitheremod.item.amphithere_armor.diamond.*;
import amphitheremod.item.amphithere_armor.gold.*;
import amphitheremod.item.amphithere_armor.iron.*;
import amphitheremod.item.amphithere_armor.silver.*;
import amphitheremod.item.amphithere_beak_attachment.*;
import amphitheremod.item.ItemAmphithereEgg;
import amphitheremod.util.CreativeTabSorter;
import amphitheremod.util.EnumAmphiType;
import amphitheremod.util.IceAndFireUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static amphitheremod.AmphithereMod.modIdWithDot;

@Mod.EventBusSubscriber(modid = AmphithereMod.MODID)
public class ModItemRegistry {

    // CREATIVE TAB
    public static final CreativeTabs AMPHITHERE_MOD_TAB = new CreativeTabs(modIdWithDot + "amphithere_mod_items") {
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(SHIVAXI_FEATHER);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            super.displayAllRelevantItems(items);
            items.sort(CreativeTabSorter::compareItems);
        }
    };

    // MATERIALS
    public static Item.ToolMaterial copperBeak = EnumHelper.addToolMaterial("Copper", 2, 190, 5.0F, 1.5F, 10);
    public static ItemArmor.ArmorMaterial copperArmor = EnumHelper.addArmorMaterial("Copper", "iceandfire:armor_copper_metal", 10, new int[]{1, 3, 4, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F);

    public static Item.ToolMaterial silverBeak = EnumHelper.addToolMaterial("Silver", 2, 460, 11.0F, 1.0F, 18);
    public static ItemArmor.ArmorMaterial silverArmor = EnumHelper.addArmorMaterial("Silver", "iceandfire:armor_silver_metal", 15, new int[]{1, 4, 5, 2}, 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F);

    // ITEMS
    final static List<Item> itemsToRegister = new ArrayList<>();
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
    public static Item AMPHITHERE_MUSIC_DISC;
    public static final Map<EnumAmphiType, Item> AMPHITHERE_EGGS = new HashMap<>();

    @SubscribeEvent
    public static void registerItemEvent(RegistryEvent.Register<Item> event) {
        if (ConfigHandler.amphithereEgg.enableAmphithereEggs) {
            for (EnumAmphiType eggVariant : EnumAmphiType.values()) {
                Item eggItem = new ItemAmphithereEgg("amphithere_" + eggVariant.name().toLowerCase() + "_egg", eggVariant, AMPHITHERE_MOD_TAB);
                itemsToRegister.add(eggItem);
                AMPHITHERE_EGGS.put(eggVariant, eggItem);
            }
        }

        itemsToRegister.add(SHIVAXI_FEATHER = new ItemShivaxiFeather("shivaxi_feather", AMPHITHERE_MOD_TAB));
        //itemsToRegister.add(AMPHITHERE_MUSIC_DISC = new ItemAmphithereMusicDisc("amphithere_music_disc", AMPHITHERE_MOD_TAB));

        if (ConfigHandler.xxlCookieBuffs.enableXxlCookie)
            itemsToRegister.add(XXL_CHOCOLATE_COOKIE = new ItemXXLChocolateCookie("xxl_chocolate_cookie", AMPHITHERE_MOD_TAB));

        //if (Loader.isModLoaded("iceandfire") && IceAndFireUtil.getIceAndFireVersion() == IceAndFireUtil.IceAndFireVersion.RLCRAFT)
        if (ConfigHandler.general.enableCrystalFeather)
            itemsToRegister.add(AMPHITHERE_CRYSTAL_FEATHER = new ItemAmphithereCrystalFeather("amphithere_crystal_feather", AMPHITHERE_MOD_TAB));

        if (ConfigHandler.amphithereArmor.enableAmphithereArmor) {
            itemsToRegister.add(AMPHITHERE_COPPER_HEAD_ARMOR = new HeadCopperArmor(copperArmor, EntityEquipmentSlot.HEAD, "copper_head_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_COPPER_WING_ARMOR = new WingCopperArmor(copperArmor, EntityEquipmentSlot.LEGS, "copper_wing_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_COPPER_BODY_ARMOR = new BodyCopperArmor(copperArmor, EntityEquipmentSlot.CHEST, "copper_body_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_COPPER_TAIL_ARMOR = new TailCopperArmor(copperArmor, EntityEquipmentSlot.FEET, "copper_tail_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_COPPER_BEAK_ATTACHMENT = new CopperBeak(copperBeak, "copper_beak_attachment", AMPHITHERE_MOD_TAB));
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
            itemsToRegister.add(AMPHITHERE_SILVER_HEAD_ARMOR = new HeadSilverArmor(silverArmor, EntityEquipmentSlot.HEAD, "silver_head_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_SILVER_WING_ARMOR = new WingSilverArmor(silverArmor, EntityEquipmentSlot.LEGS, "silver_wing_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_SILVER_TAIL_ARMOR = new TailSilverArmor(silverArmor, EntityEquipmentSlot.FEET, "silver_tail_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_SILVER_BODY_ARMOR = new BodySilverArmor(silverArmor, EntityEquipmentSlot.CHEST, "silver_body_armor", AMPHITHERE_MOD_TAB));
            itemsToRegister.add(AMPHITHERE_SILVER_BEAK_ATTACHMENT = new SilverBeak(silverBeak, "silver_beak_attachment", AMPHITHERE_MOD_TAB));
        }

        event.getRegistry().registerAll(itemsToRegister.toArray(new Item[0]));
    }

    public static void init() {
    }
}