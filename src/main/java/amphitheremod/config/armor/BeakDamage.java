package amphitheremod.config.armor;

import net.minecraftforge.common.config.Config;

public class BeakDamage {
    @Config.Comment("Divides Amphithere Beak damage by given value. 0.5 being 50% decrease in damage.")
    @Config.Name("Amphithere Beak Damage Divider")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.1f, max = 1)
    public double beakDamageDivider = 0.5f;

    @Config.Comment("Enables custom beak damages")
    @Config.Name("Enable Cusotm Beak Damage")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.0f, max = 100)
    public boolean enableCustomBeakDamage = false;

    @Config.Comment("Sets damage for copper beak")
    @Config.Name("Copper Beak Damage")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.0f, max = 100)
    public float copperBeakDamage = 1;

    @Config.Comment("Sets damage for iron beak")
    @Config.Name("Iron Beak Damage")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.0f, max = 100)
    public float ironBeakDamage = 2;

    @Config.Comment("Sets damage for gold beak")
    @Config.Name("Gold Beak Damage")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.0f, max = 100)
    public float goldBeakDamage = 3;

    @Config.Comment("Sets damage for diamond beak")
    @Config.Name("Diamond Beak Damage")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.0f, max = 100)
    public float diamondBeakDamage = 4;

    @Config.Comment("Sets damage for silver beak")
    @Config.Name("Silver Beak Damage")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.0f, max = 100)
    public float silverBeakDamage = 5;

    @Config.Comment("Sets damage for shocked beak")
    @Config.Name("Silver Beak Damage")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.0f, max = 100)
    public float shockedBeakDamage = 6;

    @Config.Comment("Sets damage for iced beak")
    @Config.Name("Silver Beak Damage")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.0f, max = 100)
    public float icedBeakDamage = 6;

    @Config.Comment("Sets damage for flamed beak")
    @Config.Name("Silver Beak Damage")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = 0.0f, max = 100)
    public float flamedBeakDamage = 6;
}
