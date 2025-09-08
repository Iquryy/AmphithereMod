package amphitheremod.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketChangeAmphithereAI implements IMessage {

    private int amphithereId;

    public PacketChangeAmphithereAI() {
    }

    public PacketChangeAmphithereAI(int amphithereId) {
        this.amphithereId = amphithereId;
    }

    public int getAmphithereId() {
        return this.amphithereId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.amphithereId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.amphithereId);
    }
}