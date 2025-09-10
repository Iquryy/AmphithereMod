package amphitheremod.network;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChangeAmphithereAIHandler implements IMessageHandler<PacketChangeAmphithereAI, IMessage> {

    @Override
    public IMessage onMessage(PacketChangeAmphithereAI message, MessageContext ctx) {
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
        serverPlayer.getServer().addScheduledTask(() -> {
            World world = serverPlayer.world;
            Entity entity = world.getEntityByID(message.getAmphithereId());

            if (entity instanceof EntityAmphithere) {
                EntityAmphithere amphithere = (EntityAmphithere) entity;
                if (amphithere.isOwner(serverPlayer) && amphithere.isTamed()) {
                    int command = amphithere.getCommand();
                    if (command < 2)
                        amphithere.setCommand(command + 1);
                    else
                        amphithere.setCommand(0);
                }
            }
        });
        return null;
    }
}