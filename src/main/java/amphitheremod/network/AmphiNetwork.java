package amphitheremod.network;

import amphitheremod.AmphithereMod;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

/** The mod's network channel. Only one packet: the inventory screen's command button. */
public final class AmphiNetwork {
    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(AmphithereMod.MODID, "main"),
            () -> VERSION, VERSION::equals, VERSION::equals);

    private AmphiNetwork() {
    }

    public static void register() {
        CHANNEL.registerMessage(0, ChangeCommand.class, ChangeCommand::encode, ChangeCommand::decode, ChangeCommand::handle);
    }

    /** Client -> server: set the amphithere's command (0 wander, 1 sit, 2 follow). */
    public record ChangeCommand(int entityId, int command) {
        static void encode(ChangeCommand msg, FriendlyByteBuf buf) {
            buf.writeVarInt(msg.entityId);
            buf.writeVarInt(msg.command);
        }

        static ChangeCommand decode(FriendlyByteBuf buf) {
            return new ChangeCommand(buf.readVarInt(), buf.readVarInt());
        }

        static void handle(ChangeCommand msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null) {
                    return;
                }
                Entity entity = player.level().getEntity(msg.entityId);
                // Only the owner, close by, can change it (the client could send any entity id)
                if (!(entity instanceof EntityAmphithere amphi) || !amphi.isOwnedBy(player) || amphi.distanceToSqr(player) > 100.0) {
                    return;
                }
                int command = Math.floorMod(msg.command, 3);
                amphi.setCommand(command);
                // Same chat line Ice and Fire shows when cycling the command by hand
                player.displayClientMessage(Component.translatable("amphithere.command." + command), true);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
