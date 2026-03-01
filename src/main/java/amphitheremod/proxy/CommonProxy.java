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
    public static SoundEvent AMPHITHERE_HURT_0 = null;
    public static SoundEvent AMPHITHERE_HURT_1 = null;
    public static SoundEvent AMPHITHERE_HURT_2 = null;
    public static SoundEvent AMPHITHERE_HURT_3 = null;
    public static SoundEvent AMPHITHERE_HURT_4 = null;
    public static SoundEvent AMPHITHERE_HURT_5 = null;
    public static SoundEvent AMPHITHERE_HURT_6 = null;
    public static SoundEvent AMPHITHERE_HURT_7 = null;
    public static SoundEvent AMPHITHERE_HURT_8 = null;
    public static SoundEvent AMPHITHERE_HURT_9 = null;

    public void preInit(FMLPreInitializationEvent event) {
        int packetId = 0;
        NETWORK_WRAPPER.registerMessage(PacketChangeAmphithereAIHandler.class, PacketChangeAmphithereAI.class, packetId++, Side.SERVER);
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                AMPHITHERE_HURT_0 = createSoundEvent("amphithere_hurt_0"),
                AMPHITHERE_HURT_1 = createSoundEvent("amphithere_hurt_1"),
                AMPHITHERE_HURT_2 = createSoundEvent("amphithere_hurt_2"),
                AMPHITHERE_HURT_3 = createSoundEvent("amphithere_hurt_3"),
                AMPHITHERE_HURT_4 = createSoundEvent("amphithere_hurt_4"),
                AMPHITHERE_HURT_5 = createSoundEvent("amphithere_hurt_5"),
                AMPHITHERE_HURT_6 = createSoundEvent("amphithere_hurt_6"),
                AMPHITHERE_HURT_7 = createSoundEvent("amphithere_hurt_7"),
                AMPHITHERE_HURT_8 = createSoundEvent("amphithere_hurt_8"),
                AMPHITHERE_HURT_9 = createSoundEvent("amphithere_hurt_9")
        );
    }

    private static SoundEvent createSoundEvent(String soundName) {
        final ResourceLocation soundID = new ResourceLocation("amphitheremod", soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }

    public void init() {
    }
}