package amphitheremod.registry;

import amphitheremod.AmphithereMod;
import amphitheremod.config.AmphiConfig;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import java.util.Map;

/**
 * Recipe condition {"type": "amphitheremod:config", "option": "armor"} so recipes for disabled features don't load.
 * 1.12.2 didn't register disabled items at all, so their recipes failed to load; this keeps that result cleanly.
 */
public record ConfigCondition(String option) implements ICondition {
    public static final ResourceLocation ID = new ResourceLocation(AmphithereMod.MODID, "config");
    private static final Map<String, ForgeConfigSpec.BooleanValue> OPTIONS = Map.of(
            "armor", AmphiConfig.ENABLE_ARMOR,
            "crystal_feather", AmphiConfig.ENABLE_CRYSTAL_FEATHER,
            "xxl_cookie", AmphiConfig.ENABLE_XXL_COOKIE,
            "anti_coth_cookie", AmphiConfig.ENABLE_ANTI_COTH_COOKIE);

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        ForgeConfigSpec.BooleanValue value = OPTIONS.get(this.option);
        return value == null || value.get();
    }

    public static final class Serializer implements IConditionSerializer<ConfigCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, ConfigCondition value) {
            json.addProperty("option", value.option());
        }

        @Override
        public ConfigCondition read(JsonObject json) {
            return new ConfigCondition(GsonHelper.getAsString(json, "option"));
        }

        @Override
        public ResourceLocation getID() {
            return ID;
        }
    }
}
