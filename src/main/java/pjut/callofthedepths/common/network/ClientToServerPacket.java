package pjut.callofthedepths.common.network;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * A packet that is sent from the client to the server, and must thus implement some serverside handler
 */
public abstract class ClientToServerPacket implements IPacket {
    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> this.handleServerside(ctx.get()));
    }

    public abstract void handleServerside(NetworkEvent.Context ctx);
}
