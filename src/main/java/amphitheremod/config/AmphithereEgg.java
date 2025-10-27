package amphitheremod.config;

import net.minecraftforge.common.config.Config;

public class AmphithereEgg {
    @Config.Comment("Makes amphitheres spawn eggs instead of baby amphitheres. When egg hatches then it spawns an amphithere.")
    @Config.Name("Enable Amphithere Eggs")
    @Config.RequiresMcRestart
    public boolean enableAmphithereEggs = true;

    @Config.Comment("20 ticks is 1 second. 24000 ticks is 20 minutes.")
    @Config.Name("Amphithere Egg Hatch Time")
    public int amphithereEggHatchTime = 24000;

    @Config.Comment("If enabled then eggs will crack when taking fall damage.")
    @Config.Name("Eggs Crack From Fall Damage")
    public boolean eggCrackFallDamage = true;

    @Config.Comment("To hatch the egg needs to be on leaves.")
    @Config.Name("Egg Needs To Be On Leaves")
    public boolean needOnLeaf = true;

    @Config.Comment("To hatch the egg needs to be in a warm biome.")
    @Config.Name("Egg Needs To Be In a Warm Biome")
    public boolean warmBiome = true;
}