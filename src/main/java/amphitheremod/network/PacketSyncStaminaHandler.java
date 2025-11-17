package amphitheremod.network;

import amphitheremod.config.ConfigHandler;
import amphitheremod.util.IAmphithereData;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncStaminaHandler implements IMessageHandler<PacketSyncStamina, IMessage> {

    @Override
    public IMessage onMessage(PacketSyncStamina message, MessageContext ctx) {
        if (ctx.side != Side.CLIENT) return null;
        if (ConfigHandler.amphiStamina.enableStamina) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
                if (entity instanceof EntityAmphithere)
                    ((IAmphithereData) entity).amphiMod_master$setStamina(message.stamina);
            });
        }
        return null;
    }
}