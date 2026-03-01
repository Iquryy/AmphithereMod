package amphitheremod;

import amphitheremod.handlers.ModItemRegistry;
import amphitheremod.network.PacketHandler;
import amphitheremod.proxy.CommonProxy;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = AmphithereMod.MODID, version = AmphithereMod.VERSION, name = AmphithereMod.NAME, dependencies = "required-after: fermiumbooter@ [1.3.0,)", acceptableRemoteVersions = "*")
public class AmphithereMod {
    public static final String MODID = "amphitheremod";
    public static final String VERSION = "1.10";
    public static final String NAME = "Amphithere Mod";
    public static String modIdWithDot = AmphithereMod.MODID + ".";
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.Instance(MODID)
    public static AmphithereMod instance;

    @SidedProxy(clientSide = "amphitheremod.proxy.ClientProxy", serverSide = "amphitheremod.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    public static StatBase TAMED_AMPHITHERE_DEATH_COUNT;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        TAMED_AMPHITHERE_DEATH_COUNT = new StatBasic("stat.tamed_amphithere_death_count.name", new TextComponentTranslation("stat.tamed_amphithere_death_count.name")).registerStat();
        ModItemRegistry.init();
        PacketHandler.registerMessages();
        proxy.preInit(event);

        org.apache.logging.log4j.core.Logger coreLogger = (org.apache.logging.log4j.core.Logger) org.apache.logging.log4j.LogManager.getRootLogger();
        coreLogger.addFilter(new org.apache.logging.log4j.core.filter.AbstractFilter() {
            @Override
            public org.apache.logging.log4j.core.Filter.Result filter(org.apache.logging.log4j.core.LogEvent event) {
                if (event.getLevel() == org.apache.logging.log4j.Level.ERROR) {
                    String message = event.getMessage().getFormattedMessage();
                    if (message.contains("Parsing error loading recipe") && message.contains("amphitheremod")) {
                        Throwable t = event.getThrown();
                        if (t != null && t.getMessage() != null && t.getMessage().contains("Unknown item"))
                            return Result.DENY;
                    }
                }
                return Result.NEUTRAL;
            }
        });
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void serverInit(FMLServerStartingEvent event) {
    }

}