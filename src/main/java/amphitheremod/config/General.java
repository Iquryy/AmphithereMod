package amphitheremod.config;

import net.minecraftforge.common.config.Config;

public class General {
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

    @Config.Comment("Enable Wing Patterns for Amphitheres.")
    @Config.Name("Enable Wing Patterns")
    @Config.RequiresMcRestart
    public boolean enableWingPatterns = true;

    @Config.Comment("Divides Amphithere Armor point by given value. 0.5 being 50% decrease in armor.")
    @Config.Name("Amphithere Armor Divider")
    @Config.RangeDouble(min = 0.01f, max = 1)
    public double armorPointDivider = 0.5f;
}