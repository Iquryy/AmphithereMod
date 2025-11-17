package amphitheremod.network;

import amphitheremod.AmphithereMod;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    private static int id = 0;

    public static void registerMessages() {
        AmphithereMod.NETWORK_WRAPPER.registerMessage(
                PacketSyncStaminaHandler.class,
                PacketSyncStamina.class,
                id++,
                Side.CLIENT
        );

        AmphithereMod.NETWORK_WRAPPER.registerMessage(
                PacketChangeAmphithereAIHandler.class,
                PacketChangeAmphithereAI.class,
                id++,
                Side.SERVER
        );
    }
}