package amphitheremod.proxy;

import amphitheremod.network.PacketChangeAmphithereAI;
import amphitheremod.network.PacketChangeAmphithereAIHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static amphitheremod.AmphithereMod.NETWORK_WRAPPER;

@Mod.EventBusSubscriber(modid = "amphitheremod")
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        int packetId = 0;
        NETWORK_WRAPPER.registerMessage(PacketChangeAmphithereAIHandler.class, PacketChangeAmphithereAI.class, packetId++, Side.SERVER);
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                createSoundEvent("amphithere_hurt_0"),
                createSoundEvent("amphithere_hurt_1"),
                createSoundEvent("amphithere_hurt_2"),
                createSoundEvent("amphithere_wing_flap")
        );
    }

    private static SoundEvent createSoundEvent(String soundName) {
        final ResourceLocation soundID = new ResourceLocation("amphitheremod", soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }

    public void init() {}
}