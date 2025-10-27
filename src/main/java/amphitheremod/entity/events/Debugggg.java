package amphitheremod.entity.events;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Debugggg {
    @SubscribeEvent
    public static void tpAmphToOwnerOnLowHp(LivingHurtEvent event) {
        World world = event.getEntity().getEntityWorld();
        if (world.isRemote) return;
        Entity entity = event.getEntity();
        if (entity instanceof EntityAmphithere) {
            // --- THIS IS THE CHANGED LINE ---
            // If the damage has a true source (i.e., it IS an entity), stop here.
            if (event.getSource().getTrueSource() != null) return;

            DamageSource source = event.getSource();
            EntityPlayer player;
            EntityAmphithere amphi = (EntityAmphithere) entity;
            if (!amphi.isTamed()) return;

            // Safe cast check for owner
            if (!(amphi.getOwner() instanceof EntityPlayer)) return;
            player = (EntityPlayer) amphi.getOwner();

            // The null check is redundant because of the instanceof check, but is good practice
            if (player == null) return;

            player.sendMessage(new TextComponentString(TextFormatting.GOLD + source.getDamageType()));
        }
    }
}