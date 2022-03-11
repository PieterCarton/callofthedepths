package pjut.callofthedepths.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import pjut.callofthedepths.common.CallOfTheDepths;

import java.util.function.Function;

public class COTDPacketHandler {
    public static int id;
    public static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CallOfTheDepths.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void registerPackets() {
        register(ClimbingCapabilitySyncPacket.class, ClimbingCapabilitySyncPacket::new);
        register(ClimbingActionPacket.class, ClimbingActionPacket::new);
    }

    public static <MSG extends IPacket> void register(Class<MSG> messageType, Function<FriendlyByteBuf, MSG> decoder) {
        INSTANCE.registerMessage(id++, messageType, (pkt, buffer) -> pkt.serialize(buffer), decoder, (pkt, ctx) -> pkt.handle(ctx));
    }
}
