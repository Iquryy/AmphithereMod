package amphitheremod.proxy;

import amphitheremod.network.PacketChangeAmphithereAI;
import amphitheremod.network.PacketChangeAmphithereAIHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

import static amphitheremod.AmphithereMod.NETWORK_WRAPPER;

@Mod.EventBusSubscriber(modid = "amphitheremod")
public class CommonProxy {
    public static SoundEvent AMPHITHERE_HURT_0 = null;
    public static SoundEvent AMPHITHERE_HURT_1 = null;
    public static SoundEvent AMPHITHERE_HURT_2 = null;
    public static SoundEvent AMPHITHERE_WING_FLAP = null;
    public static SoundEvent AMPHITHERE_MUISC_DISC = null;
    //public static KeyBinding ELYTRA_DIVE_KEY;

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
                AMPHITHERE_WING_FLAP = createSoundEvent("amphithere_wing_flap"),
                AMPHITHERE_MUISC_DISC = createSoundEvent("amphithere_music_disc")
        );
    }

    private static SoundEvent createSoundEvent(String soundName) {
        final ResourceLocation soundID = new ResourceLocation("amphitheremod", soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }

    public void init() {
        /*ELYTRA_DIVE_KEY = new KeyBinding("key.amphitheremod.elytra_dive", Keyboard.KEY_LCONTROL, "key.categories.amphitheremod");
        ClientRegistry.registerKeyBinding(ELYTRA_DIVE_KEY);*/
    }
}