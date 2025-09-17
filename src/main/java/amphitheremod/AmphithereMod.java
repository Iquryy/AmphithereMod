package amphitheremod;

import amphitheremod.handlers.ModRegistry;
import amphitheremod.network.PacketChangeAmphithereAI;
import amphitheremod.network.PacketChangeAmphithereAIHandler;
import amphitheremod.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = AmphithereMod.MODID, version = AmphithereMod.VERSION, name = AmphithereMod.NAME, dependencies = "required-after: fermiumbooter@ [1.3.0,)")
public class AmphithereMod {
    public static final String MODID = "amphitheremod";
    public static final String VERSION = "1.0.3.3";
    public static final String NAME = "Amphithere Mod";
    public static String modIdWithDot = AmphithereMod.MODID + ".";
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.Instance(MODID)
    public static AmphithereMod instance;

    @SidedProxy(clientSide = "amphitheremod.proxy.ClientProxy", serverSide = "amphitheremod.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModRegistry.init();
        proxy.preInit(event);
        int packetId = 0;
        NETWORK_WRAPPER.registerMessage(PacketChangeAmphithereAIHandler.class, PacketChangeAmphithereAI.class, packetId++, Side.SERVER);
    }
}