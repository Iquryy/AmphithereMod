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

    @Config.Comment("When tamed Amphithere is attacked and under 15hp (or custom set amount) it will teleport to the owner player.")
    @Config.Name("Teleport Low HP Amphitheres To Owner")
    public boolean tpAmphOnLowHp = true;

    @Config.Comment("What health and below the Amphithere will teleport to the owner")
    @Config.Name("Teleport Amphithere Health Threshold")
    @Config.RangeDouble(min = 1f, max = 30f)
    public double hpThresh = 15f;
}