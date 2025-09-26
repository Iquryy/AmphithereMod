package amphitheremod.config;

import amphitheremod.AmphithereMod;
import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = AmphithereMod.MODID)
public class ConfigHandler {
    @Config.Comment("General settings for core Amphithere mechanics.")
    @Config.Name("General Settings")
    public static final General general = new General();

    @Config.Comment("Settings related to XXL Chocolate Cookie buff.")
    @Config.Name("XXL Chocolate Cookie")
    public static final XxlCookieBuffs xxlCookieBuffs = new XxlCookieBuffs();

    @Config.Comment("Settings for Shivaxi Amphithere.")
    @Config.Name("Shivaxi Amphithere")
    public static final ShivaxiSettings shivaxi = new ShivaxiSettings();

    @Config.Comment("Mixins Options & Toggles")
    @Config.Name("Mixin Options")
    public static final Mixins mixins = new Mixins();

    public static class General {
        @Config.Comment("Enable or disable the crafting and use of Amphithere Armor.")
        @Config.Name("Enable Amphithere Armor")
        @Config.RequiresMcRestart
        public boolean enableAmphithereArmor = true;

        @Config.Comment("Enabling and Disabling Crystal Feather")
        @Config.Name("Enable Crystal Feather")
        @Config.RequiresMcRestart
        public boolean enableCrystalFeather = true;

        @Config.Comment("If this is set to true then all armor and beaks are cosmetic items that give no extra armor or damage to Amphitheres")
        @Config.Name("Make Armor/Beak a Cosmetic")
        public boolean cosmeticArmorBeak = false;

        @Config.Comment("Enables a set bonus for full silver armor on Amphitheres, granting the 'Cure' effect if PotionCore is installed.")
        @Config.Name("Enable Silver Armor Set Bonus")
        public boolean enableSilverSetBonus = true;

        @Config.Comment("Can only Male with Female Amphithere breed with each other")
        @Config.Name("Male + Female Breeding")
        public boolean maleAndFemale = true;
    }

    @MixinConfig(name = AmphithereMod.MODID)
    @SuppressWarnings("unused")
    public static class Mixins {
        @Config.Comment({
                "Defines the healing amount of Cocoa Beans.",
                "An Amphithere's max health is divided by this number to determine the healing amount.",
                "Default: 10 (heals 10% of max HP)."
        })
        @Config.Name("Cocoa Bean Healing Divisor")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 2, max = 20)
        public int amphithereHealDivisor = 10;

        @Config.Comment("With this mixin, the dragon 3rd person view in F5 can also be used with amphis")
        @Config.Name("Enable Amphithere View")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.amphiview.json", defaultValue = true)
        public boolean amphiView = true;

        @Config.Comment("Sets the third-person camera view distance when riding an Amphithere.")
        @Config.Name("Amphithere Riding Camera Distance")
        @Config.RangeInt(min = 1, max = 10)
        @Config.RequiresMcRestart
        public int ridingViewDistance = 3;

        @Config.Comment("With this mixin, feeding coco beans to amphithere will heal them 10% of their max hp instead 5 fixed amount")
        @Config.Name("Enable Cocoa Bean Heal Change")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.dynamicfeeding.json", defaultValue = true)
        public boolean changeCocoaBeanHeal = true;

        @Config.Comment("Enables an inventory for tamed Amphitheres, accessible by shift-right-clicking.")
        @Config.Name("Enable Amphithere Inventory")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.inventory.json", defaultValue = true)
        public boolean enableAmphithereInventory = true;

        @Config.Comment("Can Amphithere pass trough leaves? (If true then its bad with dynamic trees leaves physics) (Completely Disabled For ROTN)")
        @Config.Name("Amphithere pass trough leaves")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.leavesphasechange.json", defaultValue = true)
        public boolean canPassTroughLeaves = false;

        @Config.Comment({
                "With this mixin Amphithere will have taming damage based from other damage increasing sources.",
                "For instance if an Amphithere has strength effect then Amphitheres taming damage will be increased.",
                "The taming damage cannot go lower from the amount in InF amphithere taming damage config.",
                "If the taming damage is 3 then it will never go lower than 3 meaning weakness does nothing."
        })
        @Config.Name("Advanced Amphithere Taming Damage (Completely Disabled For ROTN)")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.tamingdmg.json", defaultValue = false)
        public boolean amphiTamingDmg = true;
    }

    public static class XxlCookieBuffs {
        @Config.Comment("Enabling and Disabling XXL Cookie")
        @Config.Name("Enable Cookie")
        @SuppressWarnings("unused")
        public boolean enableXxlCookieBuff = true;

        @Config.Comment("Duration of the XXL Chocolate Cookie's health boost effect in seconds.")
        @Config.Name("XXL Chocolate Cookie Buff Duration (Seconds)")
        @Config.RangeInt(min = 1)
        public int xxlCookieBuffDuration = 10800;

        @Config.Comment({
                "The strength of the Health Boost effect from the XXL Chocolate Cookie.",
                "Each level adds 4 health (2 hearts)."
        })
        @Config.Name("XXL Chocolate Cookie Effect Strength")
        @Config.RangeInt(min = 1, max = 255)
        public int xxlCookieEffectLevel = 9;
    }

    public static class ShivaxiSettings {
        @Config.Comment("Enables the Shivaxi variant of the Amphithere to spawn in the world.")
        @Config.Name("Enable Shivaxi Amphithere")
        public boolean enableShivaxiAmphithere = true;

        @Config.Comment({
                "Sets the spawn chance for the Shivaxi Amphithere.",
                "The chance is 1 in X, where X is this number. Higher numbers make it rarer."
        })
        @Config.Name("Shivaxi Amphithere Spawn Chance")
        @Config.RangeInt(min = 1)
        public int shivaxiAmphithereChance = 1000;

        @Config.Comment("The base maximum health of the Shivaxi Amphithere.")
        @Config.Name("Shivaxi Amphithere Max Health")
        @Config.RangeDouble(min = 1.0)
        public float shivaxiAmphithereHealth = 500;

        @Config.Comment("The base attack damage of the Shivaxi Amphithere.")
        @Config.Name("Shivaxi Amphithere Attack Damage")
        @Config.RangeDouble(min = 0.0)
        public float shivaxiAmphithereDamage = 69;
    }

    @Mod.EventBusSubscriber(modid = AmphithereMod.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(AmphithereMod.MODID)) {
                ConfigManager.sync(AmphithereMod.MODID, Config.Type.INSTANCE);
            }
        }
    }
}