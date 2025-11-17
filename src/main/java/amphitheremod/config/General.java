package amphitheremod.config;

import net.minecraftforge.common.config.Config;

public class General {
    @Config.Comment("Enabling and Disabling Crystal Feather")
    @Config.Name("Enable Crystal Feather")
    @Config.RequiresMcRestart
    public boolean enableCrystalFeather = true;

    @Config.Comment("Can only Male with Female Amphithere breed with each other")
    @Config.Name("Male + Female Breeding")
    public boolean maleAndFemale = true;

    @Config.Comment("Enable Wing Patterns for Amphitheres.")
    @Config.Name("Enable Wing Patterns")
    @Config.RequiresMcRestart
    public boolean enableWingPatterns = true;
}