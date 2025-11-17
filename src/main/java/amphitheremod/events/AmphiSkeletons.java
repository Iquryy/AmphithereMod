package amphitheremod.events;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class AmphiSkeletons {

    @SubscribeEvent
    public static void onAmphithereStruck(EntityStruckByLightningEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityAmphithere) {
            EntityAmphithere amphi = (EntityAmphithere) entity;
            World world = entity.getEntityWorld();
            if (!world.isRemote) {
                if (amphi.getRNG().nextInt(50 + 1) == 50) {
                    if (amphi.getVariant() != EnumAmphiType.getIntFromEnum(EnumAmphiType.WITHER_SKELETON) || amphi.getVariant() != EnumAmphiType.getIntFromEnum(EnumAmphiType.SKELETON)) {
                        amphi.playSound(SoundEvents.ENTITY_SKELETON_HURT, 2.0F, 1.0F);
                        amphi.setVariant(EnumAmphiType.getIntFromEnum(EnumAmphiType.SKELETON));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBurningAmphithere(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (entity instanceof EntityAmphithere) {
            EntityAmphithere amphi = (EntityAmphithere) entity;
            if (source == DamageSource.LAVA) {
                if (amphi.getRNG().nextInt(50 + 1) == 50) {
                    if (amphi.getVariant() != EnumAmphiType.getIntFromEnum(EnumAmphiType.WITHER_SKELETON) || amphi.getVariant() == EnumAmphiType.getIntFromEnum(EnumAmphiType.SKELETON)) {
                        amphi.playSound(SoundEvents.ENTITY_WITHER_SKELETON_HURT, 2.0F, 1.0F);
                        amphi.setVariant(EnumAmphiType.getIntFromEnum(EnumAmphiType.WITHER_SKELETON));
                    }
                }
            }
        }
    }
}