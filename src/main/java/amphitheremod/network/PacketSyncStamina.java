package amphitheremod.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSyncStamina implements IMessage {

    public int entityId;
    public int stamina;

    public PacketSyncStamina() {}

    public PacketSyncStamina(int entityId, int stamina) {
        this.entityId = entityId;
        this.stamina = stamina;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.stamina = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.stamina);
    }
}