package amphitheremod.config;

import net.minecraftforge.common.config.Config;

public class XxlCookieBuffs {
    @Config.Comment("Enabling and Disabling XXL Cookie")
    @Config.Name("Enable Cookie")
    @SuppressWarnings("unused")
    public boolean enableXxlCookie = true;

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