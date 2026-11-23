package amphitheremod;

import amphitheremod.config.AmphiClientConfig;
import amphitheremod.config.AmphiConfig;
import amphitheremod.event.CommonEvents;
import amphitheremod.event.SoulBindEvents;
import amphitheremod.network.AmphiNetwork;
import amphitheremod.registry.AmphiCreativeTab;
import amphitheremod.registry.AmphiEntities;
import amphitheremod.registry.AmphiItems;
import amphitheremod.registry.AmphiMenus;
import amphitheremod.registry.AmphiSounds;
import amphitheremod.registry.AmphiStats;
import amphitheremod.registry.ConfigCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Amphithere Mod: expands Ice and Fire's amphitheres. Most of the behavior is mixed into Ice and Fire's
 * EntityAmphithere (see amphitheremod.mixins.json); this class wires up registries, configs and events.
 */
@Mod(AmphithereMod.MODID)
public class AmphithereMod {
    public static final String MODID = "amphitheremod";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public AmphithereMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        AmphiItems.ITEMS.register(modBus);
        AmphiEntities.ENTITIES.register(modBus);
        AmphiSounds.SOUNDS.register(modBus);
        AmphiStats.STATS.register(modBus);
        AmphiMenus.MENUS.register(modBus);
        AmphiCreativeTab.TABS.register(modBus);
        modBus.addListener(AmphiEntities::onAttributes);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::registerConditions);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AmphiConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, AmphiClientConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
        MinecraftForge.EVENT_BUS.register(SoulBindEvents.class);
    }

    private void registerConditions(RegisterEvent event) {
        // Recipe conditions are registered alongside the recipe serializers
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(ConfigCondition.Serializer.INSTANCE);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        AmphiNetwork.register();
        event.enqueueWork(AmphiStats::init);
    }
}
