package amphitheremod.config.SpecialAmphitheres;

import net.minecraftforge.common.config.Config;

public class BlackEagleAmphithereOptions {
    @Config.Comment("Enables the Black Eagle variant of the Amphithere to spawn in the world.")
    @Config.Name("Enable Black Eagle Amphithere")
    public boolean enableBlackEagleAmphithere = true;

    @Config.Comment({
            "Sets the spawn chance for the Black Eagle Amphithere.",
            "The chance is 1 in X. Higher numbers make it rarer."
    })
    @Config.Name("Black Eagle Amphithere Spawn Chance.")
    @Config.RangeInt(min = 1)
    public int blackEagleAmphithereChance = 1000;

    @Config.Comment("The base maximum health of the Black Eagle Amphithere.")
    @Config.Name("Black Eagle Amphithere Max Health")
    @Config.RangeDouble(min = 1.0)
    public float blackEagleAmphithereHealth = 350;

    @Config.Comment("The base attack damage of the Black Eagle Amphithere.")
    @Config.Name("Black Eagle Amphithere Attack Damage")
    @Config.RangeDouble(min = 1.0)
    public float blackEagleAmphithereDamage = 45;
}