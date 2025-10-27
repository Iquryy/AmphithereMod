package amphitheremod.handlers;

import amphitheremod.AmphithereMod;
import amphitheremod.entity.EntityAmphithereEgg;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;


@Mod.EventBusSubscriber(modid = AmphithereMod.MODID)
public class ModEntityRegistry {

    private static int modEntityId = 0;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        registerEntity("amphithere_egg", EntityAmphithereEgg.class, 64, 3, true, event);
    }

    private static void registerEntity(String name, Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, RegistryEvent.Register<EntityEntry> event) {
        ResourceLocation registryName = new ResourceLocation(AmphithereMod.MODID, name);
        EntityEntry entry = EntityEntryBuilder.create().entity(entityClass).id(registryName, modEntityId++).name(name).tracker(trackingRange, updateFrequency, sendsVelocityUpdates).build();
        event.getRegistry().register(entry);
    }
}