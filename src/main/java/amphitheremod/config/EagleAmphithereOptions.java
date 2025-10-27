package amphitheremod.config;

import net.minecraftforge.common.config.Config;

public class EagleAmphithereOptions {
    @Config.Comment("Enables the Eagle variant of the Amphithere to spawn in the world.")
    @Config.Name("Enable Eagle Amphithere")
    public boolean enableEagleAmphithere = true;

    @Config.Comment({
            "Sets the spawn chance for the Eagle Amphithere.",
            "The chance is 1 in X. Higher numbers make it rarer."
    })
    @Config.Name("Eagle Amphithere Spawn Chance")
    @Config.RangeInt(min = 1)
    public int eagleAmphithereChance = 1000;

    @Config.Comment("The base maximum health of the Eagle Amphithere.")
    @Config.Name("Eagle Amphithere Max Health")
    @Config.RangeDouble(min = 1.0)
    public float eagleAmphithereHealth = 350;

    @Config.Comment("The base attack damage of the Eagle Amphithere.")
    @Config.Name("Eagle Amphithere Attack Damage")
    @Config.RangeDouble(min = 1.0)
    public float eagleAmphithereDamage = 50;
}