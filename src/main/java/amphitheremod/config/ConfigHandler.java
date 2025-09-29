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
    public static final ShivaxiAmphithereOptions shivaxi = new ShivaxiAmphithereOptions();

    @Config.Comment("Mixins Options & Toggles")
    @Config.Name("Mixin Options")
    public static final Mixins mixins = new Mixins();

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
        @Config.RangeInt(min = 0, max = 10)
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

        @Config.Comment({
                "With this mixin Amphithere will have taming damage based from other damage increasing sources.",
                "For instance if an Amphithere has strength effect then Amphitheres taming damage will be increased.",
                "The taming damage cannot go lower from the amount in InF amphithere taming damage config.",
                "If the taming damage is 3 then it will never go lower than 3 meaning weakness does nothing."
        })
        @Config.Name("Advanced Amphithere Taming Damage")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.tamingdmg.json", defaultValue = true)
        public boolean amphiTamingDmg = true;

        @Config.Comment("Prevent Tamed Amphitheres from attacking a wild Amphithere while the owner is trying to tame it.")
        @Config.Name("Tamed Amphitheres Ignore Wild Taming")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.ignorewildtaming.json", defaultValue = true)
        public boolean tamedIgnoresWildTaming = true;


        @Config.Comment({
                "If True Amphithere can't pass trough leaves",
                "If 'Amphitheres Dynamic Trees Leave Compatibility' is enabled then it makes moving on those leaves smoother like on ground "
        })
        @Config.Name("Amphithere Can't Phase Trough Leaves")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.leavesphasechange.json", defaultValue = false)
        public boolean canPassTroughLeaves = false;

        @Config.Comment({
                "Allows Amphitheres to phase through dynamic leaves as if they were vanilla leaves.",
                "This will fix issues with Amphis getting stuck in trees. (Dynamic Trees or Dramatic Trees)"
        })
        @Config.Name("Amphitheres Dynamic Trees Leave Compatibility")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.dynleavesphasechange.json", defaultValue = true)
        public boolean canPassTroughDynamicLeaves = true;

        @Config.Comment("Allows Amphitheres to phase through dynamic branches as if they were leaves. (Dynamic Trees or Dramatic Trees)")
        @Config.Name("Amphitheres Dynamic Trees Branch Compatibility")
        @Config.RequiresMcRestart
        @MixinConfig.MixinToggle(lateMixin = "mixins.amphitheremod.dynbranchphasechange.json", defaultValue = true)
        public boolean canPassTroughDynamicBranch = true;
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