package amphitheremod.events;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static amphitheremod.AmphithereMod.TAMED_AMPHITHERE_DEATH_COUNT;

@Mod.EventBusSubscriber
public class AmphiOnDeath{
    @SubscribeEvent
    public static void onAmphithereStruck(LivingDeathEvent event) {
        World world = event.getEntity().getEntityWorld();
        if (world.isRemote) return;
        if(!(event.getEntity() instanceof EntityAmphithere)) return;
        EntityAmphithere amphi = (EntityAmphithere) event.getEntity();
        if(!amphi.isTamed() || amphi.getOwner() == null) return;
        EntityPlayer player = (EntityPlayer) amphi.getOwner();
        player.addStat(TAMED_AMPHITHERE_DEATH_COUNT);
    }
}