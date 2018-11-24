/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.network;

import fr.rpcore.thirstmod.ThirstMod;
import fr.rpcore.thirstmod.common.thirstlogic.ThirstStats;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketMovementSpeed
        implements IMessage {
    public UUID uuid;
    public int ms = 0;

    public PacketMovementSpeed() {
    }

    public PacketMovementSpeed(EntityPlayer player, ThirstStats stats) {
        this.uuid = player.getUniqueID();
        this.ms = stats.getMovementSpeed(player);
    }

    public void fromBytes(ByteBuf buf) {
        this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.ms = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuid.toString());
        buf.writeInt(this.ms);
    }

    public void handleServerSide() {
        ThirstMod.getProxy().getStatsByUUID(this.uuid).movementSpeed = this.ms;
    }

    public static class Handler
            implements IMessageHandler<PacketMovementSpeed, IMessage> {
        public IMessage onMessage(PacketMovementSpeed message, MessageContext ctx) {
            message.handleServerSide();
            return null;
        }
    }
}
