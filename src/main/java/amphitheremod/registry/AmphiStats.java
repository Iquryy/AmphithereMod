package amphitheremod.registry;

import amphitheremod.AmphithereMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/** The "Tamed Amphithere Death Count" statistic (event.CommonEvents bumps it when a tamed amphithere dies). */
public final class AmphiStats {
    public static final DeferredRegister<ResourceLocation> STATS = DeferredRegister.create(Registries.CUSTOM_STAT, AmphithereMod.MODID);
    public static final ResourceLocation TAMED_AMPHITHERE_DEATH_COUNT_ID = new ResourceLocation(AmphithereMod.MODID, "tamed_amphithere_death_count");
    public static final RegistryObject<ResourceLocation> TAMED_AMPHITHERE_DEATH_COUNT = STATS.register("tamed_amphithere_death_count",
            () -> TAMED_AMPHITHERE_DEATH_COUNT_ID);

    private AmphiStats() {
    }

    /** Vanilla only lists a custom stat once a Stat object exists for it; call during common setup. */
    public static void init() {
        Stats.CUSTOM.get(TAMED_AMPHITHERE_DEATH_COUNT_ID, StatFormatter.DEFAULT);
    }
}
