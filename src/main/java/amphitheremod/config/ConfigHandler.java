package amphitheremod.config;

import amphitheremod.AmphithereMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = AmphithereMod.MODID, name = AmphithereMod.MODID + "/" + AmphithereMod.MODID)
@Mod.EventBusSubscriber(modid = AmphithereMod.MODID)
public class ConfigHandler {

    private ConfigHandler() {}

    @Config.Comment("General settings for core Amphithere mechanics.")
    @Config.Name("General Settings")
    public static final General general = new General();

    @Config.Comment("Settings related to the powerful XXL Chocolate Cookie buff.")
    @Config.Name("XXL Chocolate Cookie")
    public static final XxlCookieBuffs xxlCookieBuffs = new XxlCookieBuffs();

    @Config.Comment("Settings for the rare and powerful Shivaxi Amphithere.")
    @Config.Name("Shivaxi Amphithere")
    public static final ShivaxiSettings shivaxi = new ShivaxiSettings();

    @Config.Comment("Mixin options")
    @Config.Name("Mixin toggles")
    @SuppressWarnings("unused")
    public static MixinToggleConfig mixins = new MixinToggleConfig();

    public static class General {
        @Config.Comment("Enable or disable the crafting and use of Amphithere Armor.")
        @Config.Name("Enable Amphithere Armor")
        @Config.RequiresMcRestart
        public boolean enableAmphithereArmor = true;

        @Config.Comment("Enables an inventory for tamed Amphitheres, accessible by shift-right-clicking.")
        @Config.Name("Enable Amphithere Inventory")
        @Config.RequiresMcRestart
        public boolean enableAmphithereInventory = true;

        @Config.Comment("Enables a set bonus for full silver armor on Amphitheres and Dragons, granting the 'Cure' effect if PotionCore is installed.")
        @Config.Name("Enable Silver Armor Set Bonus")
        public boolean enableSilverSetBonus = true;

        @Config.Comment({
                "Defines the healing power of Cocoa Beans.",
                "An Amphithere's max health is divided by this number to determine the healing amount.",
                "Default: 10 (heals 10% of max HP)."
        })
        @Config.Name("Cocoa Bean Healing Divisor")
        @Config.RangeInt(min = 2, max = 20)
        public int amphithereHealDivisor = 10;

        @Config.Comment("Sets the third-person camera distance multiplier when riding an Amphithere.")
        @Config.Name("Amphithere Riding Camera Distance")
        @Config.RangeInt(min = 1, max = 10)
        @Config.RequiresMcRestart
        public int ridingViewDistance = 2;
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
        @Config.Comment("Enables the rare and powerful Shivaxi variant of the Amphithere to spawn in the world.")
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

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(AmphithereMod.MODID)) {
            ConfigManager.sync(AmphithereMod.MODID, Config.Type.INSTANCE);
        }
    }
}