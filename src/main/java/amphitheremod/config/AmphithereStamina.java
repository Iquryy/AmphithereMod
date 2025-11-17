package amphitheremod.config;

import amphitheremod.AmphithereMod;
import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = AmphithereMod.MODID)
@SuppressWarnings("unused")
public class AmphithereStamina {

    @Config.Comment("Enables stamina system for tamed Amphitheres. If disabled, Amphitheres will have default flight mechanics..")
    @Config.Name("Enable Stamina")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(earlyMixin = "mixins.amphitheremod.stamina.json", defaultValue = true)
    public boolean enableStamina = true;

    @Config.Comment({"The base maximum stamina for a just tamed Amphithere. This value is further modified by the 'Max Stamina Tame Variance' and by Amphitheres max health."})
    @Config.Name("Base Maximum Stamina")
    @Config.RangeDouble(min = 1.0f)
    public float maxStamina = 175;

    @Config.Comment("A random percentage that increases an Amphithere's maximum stamina upon taming. The final max stamina will be increased by a random value between 0 and this number. Ex: A value of 50 means stamina can be up to 50% higher than the base.")
    @Config.Name("Max Stamina Tame Variance")
    @Config.RangeInt(min = 0)
    public int maxStaminaMulti = 75;

    @Config.Comment("The cooldown in seconds before stamina begins to regenerate after being fully depleted. This prevents immediate stamina recovery after exhaustion.")
    @Config.Name("Exhaustion Cooldown")
    @Config.RangeInt(min = 0)
    public int staminaCDWhenStaminaExhaust = 10;

    @Config.Comment("The delay in seconds before stamina begins to regenerate after the Amphithere stops flapping or flying. This prevents instant stamina recovery upon landing or gliding.")
    @Config.Name("Stamina Regeneration Delay")
    @Config.RangeInt(min = 0)
    public int staminaRegCDFlapping = 6;

    @Config.Comment("Configure the amount of stamina consumed by different actions. These values are cumulative if multiple actions occur at once.")
    @Config.Name("Stamina Drain")
    public final StaminaDrain staminaDrain = new StaminaDrain();

    @Config.Comment("Only shows debug in chat when the player is riding an Amphithere.")
    @Config.Name("Stamina Drain/Regen Debug")
    public boolean staminaDebug = false;

    @Config.Comment("Configure the amount of stamina regenerated per second under different conditions. These values are cumulative if multiple conditions are met simultaneously.")
    @Config.Name("Stamina Regeneration")
    public final StaminaRegeneration staminaRegeneration = new StaminaRegeneration();

    public static class StaminaDrain {
        @Config.Comment("The flat amount of stamina consumed each time the Amphithere flaps its wings.")
        @Config.Name("Wing Flap Stamina Drain")
        @Config.RangeDouble(min = 0f)
        public float flapDrain = 12f;

        /*@Config.Comment("How much additional stamina as flap will cost from max Amphithere stamina. (150 (max stamina) * 0.01 = 1.5 stamina drain)")
        @Config.Name("Flap Drain From Max Stamina")
        @Config.RangeDouble(min = 0.0f, max = 1.0f)
        public float flapDrainFromMaxStamina = 0.01f;*/

        @Config.Comment("The amount of stamina drained per second for sustained flight (when not gliding or diving).")
        @Config.Name("Passive Flying Drain")
        @Config.RangeDouble(min = 0.0f)
        public float flyingDrainPerSecond = 4.5f;
    }

    public static class StaminaRegeneration {
        @Config.Comment({"How much should max stamina affect Amphitheres stamina regeneration. (1 = 100%, 0.5 = 50%, 0 = 0%)",
        "This is only applied when Amphithere is on ground."
        })
        @Config.Name("Stamina Reneration Amount From Max Stamina")
        @Config.RangeDouble(min = 0.0f, max = 1.0f)
        public float maxStaminaRegen = 0.01f;

        @Config.Comment("The amount of stamina regenerated per second while diving.")
        @Config.Name("Diving Regeneration Rate")
        @Config.RangeDouble(min = 0.0f)
        public float diving = 0.1f;

        @Config.Comment("The amount of stamina regenerated per second while gliding.")
        @Config.Name("Gliding Regeneration Rate")
        @Config.RangeDouble(min = 0.0f)
        public float gliding = 0.25f;

        @Config.Comment("The amount of stamina regenerated per second while on the ground.")
        @Config.Name("Grounded Regeneration Rate")
        @Config.RangeDouble(min = 0.0f)
        public float onGround = 0.3f;

        @Config.Comment("The multiplier of stamina regenerated per second when in love (after being fed cookie). (1x = 0%, 1.25x = 25%)")
        @Config.Name("In Love Stamina Regeneration Multi")
        @Config.RangeDouble(min = 0.0f)
        public float inLove = 1.45f;

        @Config.Comment("The multiplier of stamina added from max stamina when feeding Coco Beans. (1 = 100%, 0.5 = 50%, 0 = 0%)")
        @Config.Name("Coco Bean Stamina Add Amount")
        @Config.RangeDouble(min = 0.0f, max = 1.0f)
        public float staminaBeanAdd = 0.045f;
    }
}