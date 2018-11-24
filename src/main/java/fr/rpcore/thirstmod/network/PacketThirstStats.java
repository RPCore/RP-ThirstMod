/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.network;

import fr.rpcore.thirstmod.ThirstMod;
import fr.rpcore.thirstmod.common.thirstlogic.ThirstStats;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketThirstStats
        implements IMessage {
    public int thirstLevel;
    public float saturation;
    public float exhaustion;
    public boolean poisoned;

    public PacketThirstStats() {
    }

    public PacketThirstStats(ThirstStats stats) {
        this.thirstLevel = stats.thirstLevel;
        this.saturation = stats.saturation;
        this.exhaustion = stats.exhaustion;
        this.poisoned = stats.poisoned;
    }

    public void fromBytes(ByteBuf buf) {
        this.thirstLevel = buf.readInt();
        this.saturation = buf.readFloat();
        this.exhaustion = buf.readFloat();
        this.poisoned = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.thirstLevel);
        buf.writeFloat(this.saturation);
        buf.writeFloat(this.exhaustion);
        buf.writeBoolean(this.poisoned);
    }

    public void handleClientSide() {
        ThirstStats stats = ThirstMod.getClientProxy().clientStats;
        stats.thirstLevel = this.thirstLevel;
        stats.saturation = this.saturation;
        stats.exhaustion = this.exhaustion;
        stats.poisoned = this.poisoned;
    }

    public static class Handler
            implements IMessageHandler<PacketThirstStats, IMessage> {
        public IMessage onMessage(PacketThirstStats message, MessageContext ctx) {
            message.handleClientSide();
            return null;
        }
    }
}
