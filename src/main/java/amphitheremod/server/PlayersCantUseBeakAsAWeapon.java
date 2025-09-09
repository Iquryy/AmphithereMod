package amphitheremod.server;

import amphitheremod.item.amphithere_beak_attachment.BeakBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PlayersCantUseBeakAsAWeapon {
    @SubscribeEvent
    public static void Fix(AttackEntityEvent event){
        EntityPlayer player = event.getEntityPlayer();
        if(player.getHeldItemMainhand().getItem() instanceof BeakBase && player.getHeldItemMainhand() != null){
            event.setCanceled(true);
        }
    }
}
