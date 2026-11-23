package amphitheremod.registry;

import amphitheremod.AmphithereMod;
import amphitheremod.entity.EntityAmphithereEgg;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class AmphiEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AmphithereMod.MODID);

    // Same size and tracking (64 blocks, every 3 ticks) as 1.12.2
    public static final RegistryObject<EntityType<EntityAmphithereEgg>> AMPHITHERE_EGG = ENTITIES.register("amphithere_egg",
            () -> EntityType.Builder.<EntityAmphithereEgg>of(EntityAmphithereEgg::new, MobCategory.MISC)
                    .sized(0.25F, 0.35F).clientTrackingRange(4).updateInterval(3).build("amphitheremod:amphithere_egg"));

    private AmphiEntities() {
    }

    public static void onAttributes(EntityAttributeCreationEvent event) {
        event.put(AMPHITHERE_EGG.get(), EntityAmphithereEgg.bakeAttributes().build());
    }
}
