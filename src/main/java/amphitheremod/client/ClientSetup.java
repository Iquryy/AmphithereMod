package amphitheremod.client;

import amphitheremod.AmphithereMod;
import amphitheremod.client.gui.AmphithereScreen;
import amphitheremod.client.gui.StaminaHud;
import amphitheremod.client.layer.AmphiLayers;
import amphitheremod.client.render.RenderAmphithereEgg;
import amphitheremod.registry.AmphiEntities;
import amphitheremod.registry.AmphiMenus;
import com.github.alexthe666.iceandfire.client.model.ModelAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/** Client registration: renderers, the amphithere overlay layers, the inventory screen and the stamina bar. */
@Mod.EventBusSubscriber(modid = AmphithereMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(AmphiMenus.AMPHITHERE.get(), AmphithereScreen::new));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AmphiEntities.AMPHITHERE_EGG.get(), RenderAmphithereEgg::new);
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        // Ice and Fire's renderer for its amphithere; the layers draw this mod's overlays over its model
        LivingEntityRenderer<EntityAmphithere, ModelAmphithere> renderer = event.getRenderer(IafEntityRegistry.AMPHITHERE.get());
        if (renderer != null) {
            AmphiLayers.addTo(renderer);
        } else {
            AmphithereMod.LOGGER.warn("No amphithere renderer found; amphithere overlays (eyes, armor, markings) won't show");
        }
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "amphithere_stamina", new StaminaHud());
    }
}
