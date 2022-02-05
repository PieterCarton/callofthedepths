package pjut.callofthedepths.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import pjut.callofthedepths.common.capability.ClimbingTracker;
import pjut.callofthedepths.common.capability.ClimbingTrackerCapability;

public class ClimbingCapabilitySyncPacket extends BidirectionalPacket{

    private int jumps;
    private double stableHeight;

    public ClimbingCapabilitySyncPacket(FriendlyByteBuf buffer) {
        this.jumps = buffer.readInt();
        this.stableHeight = buffer.readDouble();
    }

    @Override
    public void handleClientside(NetworkEvent.Context ctx) {
        LocalPlayer player = Minecraft.getInstance().player;
        LazyOptional<ClimbingTracker> capability = ClimbingTrackerCapability.get(player);
        capability.ifPresent(cap -> {
            cap.setJumps(this.jumps);
            cap.setStableHeight(this.stableHeight);
        });
    }

    @Override
    public void handleServerside(NetworkEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        LazyOptional<ClimbingTracker> capability = ClimbingTrackerCapability.get(player);
        capability.ifPresent(cap -> {
            cap.setJumps(this.jumps);
            cap.setStableHeight(this.stableHeight);
        });
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(this.jumps);
        buffer.writeDouble(this.stableHeight);
    }
}
