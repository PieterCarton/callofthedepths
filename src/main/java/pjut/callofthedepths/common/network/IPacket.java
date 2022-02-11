package pjut.callofthedepths.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket {

    void handle(Supplier<NetworkEvent.Context> ctx);

    void serialize(FriendlyByteBuf buffer);
}
