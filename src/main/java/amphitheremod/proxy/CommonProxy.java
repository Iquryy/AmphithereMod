package amphitheremod.proxy;

import amphitheremod.network.PacketChangeAmphithereAI;
import amphitheremod.network.PacketChangeAmphithereAIHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import static amphitheremod.AmphithereMod.NETWORK_WRAPPER;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        int packetId = 0;
        NETWORK_WRAPPER.registerMessage(PacketChangeAmphithereAIHandler.class, PacketChangeAmphithereAI.class, packetId++, Side.SERVER);
    }

}