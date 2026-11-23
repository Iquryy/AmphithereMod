package amphitheremod.config;

import net.minecraftforge.common.ForgeConfigSpec;

/** amphitheremod-client.toml: camera and HUD options that only matter to the player's own game. */
public final class AmphiClientConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue AMPHI_VIEW;
    public static final ForgeConfigSpec.IntValue RIDING_VIEW_DISTANCE;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("camera");
        AMPHI_VIEW = b.comment("Third person camera while riding an Amphithere: pulled back, and Ice and Fire's dragon view key",
                "(left/centre/right) also works for Amphitheres.").define("amphiView", true);
        RIDING_VIEW_DISTANCE = b.comment("How far the third person camera is pulled back while riding an Amphithere.")
                .defineInRange("ridingViewDistance", 4, 0, 10);
        b.pop();
        SPEC = b.build();
    }

    private AmphiClientConfig() {
    }
}
