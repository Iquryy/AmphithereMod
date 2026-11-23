package amphitheremod.client;

import amphitheremod.AmphithereMod;
import amphitheremod.config.AmphiClientConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Riding camera: in third person the camera is pulled back by the configured distance, and Ice and Fire's dragon view
 * key (cycles 0..3) shifts it to the left (2) or right (3) of the amphithere, as in 1.12.2.
 */
@Mod.EventBusSubscriber(modid = AmphithereMod.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() {
    }

    @SubscribeEvent
    public static void onCamera(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || !(player.getVehicle() instanceof EntityAmphithere) || !AmphiClientConfig.AMPHI_VIEW.get()) {
            return;
        }
        CameraType type = mc.options.getCameraType();
        if (type.isFirstPerson()) {
            return;
        }
        float distance = AmphiClientConfig.RIDING_VIEW_DISTANCE.get();
        int view = IceAndFire.PROXY.getDragon3rdPersonView();
        float side = view == 2 ? distance : view == 3 ? -distance : 0.0F;
        if (type == CameraType.THIRD_PERSON_FRONT) {
            side = -side;
        }
        // getMaxZoom stops the camera at walls like vanilla's third person camera
        event.getCamera().move(-event.getCamera().getMaxZoom(distance), 0.0, side);
    }
}
