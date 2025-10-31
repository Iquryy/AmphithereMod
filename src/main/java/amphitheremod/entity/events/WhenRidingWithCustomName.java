package amphitheremod.entity.events;

import amphitheremod.util.EnumAmphiType;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber
public class WhenRidingWithCustomName {

    private static final UUID IQURY_UUID = UUID.fromString("87dd25be-5eb9-451f-a839-c74ce939b76b");
    private static final UUID CRAFTY_UUID = UUID.fromString("c6cbbc0f-3b57-4cd7-961c-42edfe6e71f3");
    private static final UUID NISCHHELM_UUID = UUID.fromString("eb8c824d-6f85-4047-8ca1-f50c8e50bfe0");

    @SubscribeEvent
    public static void assignVariant(EntityMountEvent event) {
        if (!event.isMounting()) return;
        World world = event.getEntity().getEntityWorld();
        if (world.isRemote) return;
        if (!(event.getEntityBeingMounted() instanceof EntityAmphithere) || !(event.getEntityMounting() instanceof EntityPlayer)) return;
        EntityAmphithere amphi = (EntityAmphithere) event.getEntityBeingMounted();
        EntityPlayer player = (EntityPlayer) event.getEntityMounting();
        if (!amphi.isTamed() || !amphi.isOwner(player) || !amphi.hasCustomName()) return;
        UUID playerUUID = player.getUniqueID();
        String amphiName = amphi.getCustomNameTag();
        if (playerUUID.equals(IQURY_UUID) && amphiName.equalsIgnoreCase("iqury") && amphi.getVariant() != EnumAmphiType.IQURY.ordinal()) {
            amphi.setVariant(EnumAmphiType.IQURY.ordinal());
            amphi.setCustomNameTag("");
        } else if (playerUUID.equals(CRAFTY_UUID) && amphiName.equalsIgnoreCase("crafty") && amphi.getVariant() != EnumAmphiType.CRAFTY.ordinal()) {
            amphi.setVariant(EnumAmphiType.CRAFTY.ordinal());
            amphi.setCustomNameTag("");
        } else if (playerUUID.equals(NISCHHELM_UUID) && amphiName.equalsIgnoreCase("nischhelm") && amphi.getVariant() != EnumAmphiType.RADISH.ordinal()) {
            amphi.setVariant(EnumAmphiType.RADISH.ordinal());
            amphi.setCustomNameTag("");
        }
    }
}