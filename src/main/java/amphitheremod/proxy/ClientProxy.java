package amphitheremod.proxy;
import amphitheremod.client.gui.AmphithereStaminaBarHud;
import amphitheremod.client.render.RenderAmphithereEgg;
import amphitheremod.entity.EntityAmphithereEgg;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        registerEntityRenders();
    }

    public void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityAmphithereEgg.class, RenderAmphithereEgg::new);
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(new AmphithereStaminaBarHud());
    }
}