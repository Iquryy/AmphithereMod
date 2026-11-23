package amphitheremod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

/**
 * amphitheremod-common.toml. Mirrors the 1.12.2 config categories and defaults. Values are read live (Forge configs
 * update in place), so the options that used to need a restart because they were mixin toggles now apply right away.
 */
public final class AmphiConfig {
    public static final ForgeConfigSpec SPEC;

    // General
    public static final BooleanValue ENABLE_ANTI_COTH_COOKIE;
    public static final BooleanValue MALE_AND_FEMALE;
    public static final BooleanValue ENABLE_WING_PATTERNS;
    public static final BooleanValue ENABLE_CRYSTAL_FEATHER;

    // Amphithere egg
    public static final BooleanValue ENABLE_EGGS;
    public static final IntValue EGG_HATCH_TIME;
    public static final BooleanValue EGG_CRACK_FALL;
    public static final BooleanValue EGG_NEEDS_LEAVES;
    public static final BooleanValue EGG_NEEDS_WARM_BIOME;

    // XXL cookie
    public static final BooleanValue ENABLE_XXL_COOKIE;
    public static final IntValue XXL_COOKIE_DURATION;
    public static final IntValue XXL_COOKIE_LEVEL;

    // Special amphitheres
    public static final BooleanValue ENABLE_SHIVAXI;
    public static final IntValue SHIVAXI_CHANCE;
    public static final DoubleValue SHIVAXI_HEALTH;
    public static final DoubleValue SHIVAXI_DAMAGE;
    public static final BooleanValue ENABLE_BLACK_EAGLE;
    public static final IntValue BLACK_EAGLE_CHANCE;
    public static final DoubleValue BLACK_EAGLE_HEALTH;
    public static final DoubleValue BLACK_EAGLE_DAMAGE;

    // Armor
    public static final BooleanValue ENABLE_ARMOR;
    public static final BooleanValue COSMETIC_ARMOR;
    public static final BooleanValue SILVER_SET_BONUS;
    public static final DoubleValue ARMOR_DIVIDER;
    public static final DoubleValue BEAK_DAMAGE_DIVIDER;
    public static final BooleanValue CUSTOM_BEAK_DAMAGE;
    public static final DoubleValue COPPER_BEAK_DAMAGE;
    public static final DoubleValue IRON_BEAK_DAMAGE;
    public static final DoubleValue GOLD_BEAK_DAMAGE;
    public static final DoubleValue DIAMOND_BEAK_DAMAGE;
    public static final DoubleValue SILVER_BEAK_DAMAGE;
    public static final DoubleValue DRAGONBONE_BEAK_DAMAGE;
    public static final DoubleValue SHOCKED_BEAK_DAMAGE;
    public static final DoubleValue ICED_BEAK_DAMAGE;
    public static final DoubleValue FLAMED_BEAK_DAMAGE;

    // Former mixin toggles
    public static final IntValue HEAL_DIVISOR;
    public static final BooleanValue CHANGE_COCOA_HEAL;
    public static final BooleanValue ENABLE_INVENTORY;
    public static final BooleanValue ADVANCED_TAMING_DAMAGE;
    public static final BooleanValue IGNORE_WILD_TAMING;
    public static final BooleanValue CANT_PASS_LEAVES;
    public static final BooleanValue PASS_DYNAMIC_LEAVES;
    public static final BooleanValue PASS_DYNAMIC_BRANCHES;

    // Stamina
    public static final BooleanValue ENABLE_STAMINA;
    public static final DoubleValue MAX_STAMINA;
    public static final IntValue MAX_STAMINA_VARIANCE;
    public static final IntValue EXHAUSTION_COOLDOWN;
    public static final IntValue REGEN_DELAY;
    public static final DoubleValue FLAP_DRAIN;
    public static final DoubleValue FLYING_DRAIN;
    public static final BooleanValue STAMINA_DEBUG;
    public static final DoubleValue REGEN_FROM_MAX;
    public static final DoubleValue REGEN_DIVING;
    public static final DoubleValue REGEN_GLIDING;
    public static final DoubleValue REGEN_GROUND;
    public static final DoubleValue REGEN_IN_LOVE;
    public static final DoubleValue BEAN_STAMINA;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.comment("General settings for core Amphithere mechanics.").push("general");
        ENABLE_ANTI_COTH_COOKIE = b.comment("Enables the Bloody Cookie. Feeding it to an Amphithere makes it permanently immune to COTH.",
                "Needs a mod that adds the COTH effect (End-Parasitize and Convert All).").define("enableAntiCothCookie", true);
        MALE_AND_FEMALE = b.comment("Only a male and a female Amphithere can breed with each other. Also shows the gender markings.")
                .define("maleAndFemale", true);
        ENABLE_WING_PATTERNS = b.comment("Wild Amphitheres can spawn with wing patterns (1 in 10).").define("enableWingPatterns", true);
        ENABLE_CRYSTAL_FEATHER = b.comment("Enables the Crystal Feather. Using it on your own tamed Amphithere soul binds it to you: if either of you dies, both die.")
                .define("enableCrystalFeather", true);
        b.pop();

        b.comment("Amphithere egg settings.").push("amphithereEgg");
        ENABLE_EGGS = b.comment("Breeding lays an egg instead of spawning a baby. The egg hatches into an Amphithere.").define("enableAmphithereEggs", true);
        // 1.12.2's default was 24000 but its egg aged twice per tick, so eggs really hatched after 12000 ticks (10 minutes);
        // the egg now ages once per tick and the default is halved so hatching takes as long as it did
        EGG_HATCH_TIME = b.comment("Ticks an egg has to stay warm before it hatches. 20 ticks is 1 second, 12000 ticks is 10 minutes.")
                .defineInRange("amphithereEggHatchTime", 12000, 1, Integer.MAX_VALUE);
        EGG_CRACK_FALL = b.comment("Eggs crack (no item drop) when they take fall damage.").define("eggCrackFallDamage", true);
        EGG_NEEDS_LEAVES = b.comment("The egg has to sit on leaves to hatch.").define("needOnLeaf", true);
        EGG_NEEDS_WARM_BIOME = b.comment("The egg has to be in a warm biome (temperature 0.8 or more) to hatch.").define("warmBiome", true);
        b.pop();

        b.comment("XXL Chocolate Cookie settings.").push("xxlChocolateCookie");
        ENABLE_XXL_COOKIE = b.comment("Enables the XXL Chocolate Cookie.").define("enableXxlCookie", true);
        XXL_COOKIE_DURATION = b.comment("Duration of the cookie's Health Boost effect in seconds.").defineInRange("xxlCookieBuffDuration", 10800, 1, 1000000);
        XXL_COOKIE_LEVEL = b.comment("Health Boost amplifier from the cookie. Each level adds 4 health (2 hearts).").defineInRange("xxlCookieEffectLevel", 9, 1, 255);
        b.pop();

        b.comment("Settings for the Shivaxi Amphithere.").push("shivaxiAmphithere");
        ENABLE_SHIVAXI = b.comment("Lets the Shivaxi Amphithere spawn in the world.").define("enableShivaxiAmphithere", true);
        SHIVAXI_CHANCE = b.comment("Spawn chance, 1 in X.").defineInRange("shivaxiAmphithereChance", 1000, 1, Integer.MAX_VALUE);
        SHIVAXI_HEALTH = b.comment("Max health.").defineInRange("shivaxiAmphithereHealth", 500.0, 1.0, 1024.0);
        SHIVAXI_DAMAGE = b.comment("Attack damage.").defineInRange("shivaxiAmphithereDamage", 69.0, 1.0, 2048.0);
        b.pop();

        b.comment("Settings for the Black Eagle Amphithere.").push("blackEagleAmphithere");
        ENABLE_BLACK_EAGLE = b.comment("Lets the Black Eagle Amphithere spawn in the world.").define("enableBlackEagleAmphithere", true);
        BLACK_EAGLE_CHANCE = b.comment("Spawn chance, 1 in X.").defineInRange("blackEagleAmphithereChance", 1000, 1, Integer.MAX_VALUE);
        BLACK_EAGLE_HEALTH = b.comment("Max health.").defineInRange("blackEagleAmphithereHealth", 350.0, 1.0, 1024.0);
        BLACK_EAGLE_DAMAGE = b.comment("Attack damage.").defineInRange("blackEagleAmphithereDamage", 45.0, 1.0, 2048.0);
        b.pop();

        b.comment("Settings for Amphithere armor and beak attachments.").push("amphithereArmor");
        ENABLE_ARMOR = b.comment("Enables crafting and equipping Amphithere armor and beaks.").define("enableAmphithereArmor", true);
        COSMETIC_ARMOR = b.comment("Armor and beaks are cosmetic only and give no armor or damage.").define("cosmeticArmorBeak", false);
        SILVER_SET_BONUS = b.comment("A full silver set (beak, head, body, wings, tail) gives the 'Cure' effect if a mod adds one (potioncore:cure).")
                .define("enableSilverSetBonus", true);
        ARMOR_DIVIDER = b.comment("Multiplies the armor points of Amphithere armor. 0.5 halves them.").defineInRange("armorPointDivider", 0.5, 0.1, 1.0);
        b.push("beakDamage");
        BEAK_DAMAGE_DIVIDER = b.comment("Multiplies beak damage (when custom beak damage is off). 0.5 halves it.").defineInRange("beakDamageDivider", 0.5, 0.1, 1.0);
        CUSTOM_BEAK_DAMAGE = b.comment("Use the damage values below instead of the material-based damage.").define("enableCustomBeakDamage", false);
        COPPER_BEAK_DAMAGE = b.defineInRange("copperBeakDamage", 1.0, 0.0, 100.0);
        IRON_BEAK_DAMAGE = b.defineInRange("ironBeakDamage", 2.0, 0.0, 100.0);
        GOLD_BEAK_DAMAGE = b.defineInRange("goldBeakDamage", 3.0, 0.0, 100.0);
        DIAMOND_BEAK_DAMAGE = b.defineInRange("diamondBeakDamage", 4.0, 0.0, 100.0);
        SILVER_BEAK_DAMAGE = b.defineInRange("silverBeakDamage", 5.0, 0.0, 100.0);
        // 1.12.2 had no custom value for the plain dragonbone beak, so it got 0 with custom damage on; it now has its own
        DRAGONBONE_BEAK_DAMAGE = b.defineInRange("dragonboneBeakDamage", 5.0, 0.0, 100.0);
        SHOCKED_BEAK_DAMAGE = b.defineInRange("shockedBeakDamage", 6.0, 0.0, 100.0);
        ICED_BEAK_DAMAGE = b.defineInRange("icedBeakDamage", 6.0, 0.0, 100.0);
        FLAMED_BEAK_DAMAGE = b.defineInRange("flamedBeakDamage", 6.0, 0.0, 100.0);
        b.pop();
        b.pop();

        b.comment("Behavior toggles (these were mixin toggles in 1.12.2).").push("mixinOptions");
        HEAL_DIVISOR = b.comment("Cocoa beans heal max health divided by this number (10 heals 10%).").defineInRange("amphithereHealDivisor", 10, 2, 20);
        CHANGE_COCOA_HEAL = b.comment("Cocoa beans heal a share of max health (above) and restore some stamina instead of a flat 5 health.")
                .define("changeCocoaBeanHeal", true);
        ENABLE_INVENTORY = b.comment("Tamed Amphitheres get an inventory (armor and beak slots), opened by the owner shift-right-clicking.")
                .define("enableAmphithereInventory", true);
        ADVANCED_TAMING_DAMAGE = b.comment("The damage an untamed Amphithere does to its rider scales with its attack damage (Strength raises it).",
                "It never goes below Ice and Fire's amphithereTameDamage.").define("amphiTamingDmg", true);
        IGNORE_WILD_TAMING = b.comment("Tamed Amphitheres don't attack a wild Amphithere their owner is riding to tame.").define("tamedIgnoresWildTaming", true);
        CANT_PASS_LEAVES = b.comment("Amphitheres can't fly through leaves (Ice and Fire lets them by default).").define("canPassTroughLeaves", false);
        PASS_DYNAMIC_LEAVES = b.comment("Amphitheres pass through Dynamic Trees leaves without being slowed or hurt.").define("canPassTroughDynamicLeaves", true);
        PASS_DYNAMIC_BRANCHES = b.comment("Amphitheres pass through Dynamic Trees branches (not trunks) like leaves.").define("canPassTroughDynamicBranch", false);
        b.pop();

        b.comment("Stamina for tamed Amphitheres while a player rides them.").push("stamina");
        ENABLE_STAMINA = b.comment("Enables stamina. Off means normal Ice and Fire flight.").define("enableStamina", true);
        MAX_STAMINA = b.comment("Base max stamina when tamed. Half the max health is added, then the tame variance.").defineInRange("maxStamina", 175.0, 1.0, 100000.0);
        MAX_STAMINA_VARIANCE = b.comment("Max stamina is raised by a random 0 to X percent when tamed.").defineInRange("maxStaminaMulti", 75, 0, 1000);
        EXHAUSTION_COOLDOWN = b.comment("Seconds before stamina starts regenerating after running out.").defineInRange("staminaCDWhenStaminaExhaust", 10, 0, 3600);
        REGEN_DELAY = b.comment("Seconds before stamina starts regenerating after flapping or flying.").defineInRange("staminaRegCDFlapping", 6, 0, 3600);
        b.push("staminaDrain");
        FLAP_DRAIN = b.comment("Stamina used per wing flap.").defineInRange("flapDrain", 12.0, 0.0, 100000.0);
        FLYING_DRAIN = b.comment("Stamina used per second of powered flight (not gliding or diving).").defineInRange("flyingDrainPerSecond", 4.5, 0.0, 100000.0);
        b.pop();
        STAMINA_DEBUG = b.comment("Shows stamina numbers in chat while riding.").define("staminaDebug", false);
        b.push("staminaRegeneration");
        REGEN_FROM_MAX = b.comment("Share of max stamina regenerated per second on the ground.").defineInRange("maxStaminaRegen", 0.01, 0.0, 1.0);
        REGEN_DIVING = b.comment("Stamina per second while diving.").defineInRange("diving", 0.1, 0.0, 100000.0);
        REGEN_GLIDING = b.comment("Stamina per second while gliding.").defineInRange("gliding", 0.25, 0.0, 100000.0);
        REGEN_GROUND = b.comment("Stamina per second on the ground.").defineInRange("onGround", 0.3, 0.0, 100000.0);
        REGEN_IN_LOVE = b.comment("Regeneration multiplier while in love (after a cookie).").defineInRange("inLove", 1.45, 0.0, 100.0);
        BEAN_STAMINA = b.comment("Share of max stamina restored by a cocoa bean.").defineInRange("staminaBeanAdd", 0.045, 0.0, 1.0);
        b.pop();
        b.pop();

        SPEC = b.build();
    }

    private AmphiConfig() {
    }
}
