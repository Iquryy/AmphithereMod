package amphitheremod.config;

import net.minecraftforge.common.config.Config;

public class ShivaxiAmphithereOptions {
    @Config.Comment("Enables the Shivaxi variant of the Amphithere to spawn in the world.")
    @Config.Name("Enable Shivaxi Amphithere")
    public boolean enableShivaxiAmphithere = true;

    @Config.Comment({
            "Sets the spawn chance for the Shivaxi Amphithere.",
            "The chance is 1 in X. Higher numbers make it rarer."
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
    @Config.RangeDouble(min = 1.0)
    public float shivaxiAmphithereDamage = 69;
}