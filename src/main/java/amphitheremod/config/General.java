package amphitheremod.config;

import net.minecraftforge.common.config.Config;

public class General {
    @Config.Comment("Enabling and Disabling Anti Coth Cookie. Feeding this to an Amphithere will make them coth immune. You need SRParasites for this item to exist.")
    @Config.Name("Enable Anti Coth Cookie")
    @Config.RequiresMcRestart
    public boolean enableAntiCothCookie = true;

    @Config.Comment("Can only Male with Female Amphithere breed with each other")
    @Config.Name("Male + Female Breeding")
    public boolean maleAndFemale = true;

    @Config.Comment("Enable Wing Patterns for Amphitheres.")
    @Config.Name("Enable Wing Patterns")
    @Config.RequiresMcRestart
    public boolean enableWingPatterns = true;
}