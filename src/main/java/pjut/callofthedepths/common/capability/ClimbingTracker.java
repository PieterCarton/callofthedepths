package pjut.callofthedepths.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;

public class ClimbingTracker {
    private Direction attachDirection = Direction.NORTH;
    private double stableHeight = Double.MAX_VALUE;
    private int jumps = 0;

    // TODO: not synced
    private boolean isSliding = false;
    private boolean isClimbing = false;

    public ClimbingTracker() {}

    public double getStableHeight() {
        return stableHeight;
    }

    public void setStableHeight(double stableHeight) {
        this.stableHeight = stableHeight;
    }

    public int getJumps() {
        return jumps;
    }

    public boolean isSliding() {
        return isSliding;
    }

    public void setSliding(boolean sliding) {
        isSliding = sliding;
    }

    public boolean isClimbing() {
        return isClimbing;
    }

    public void setClimbing(boolean climbing) {
        isClimbing = climbing;
    }

    public void setAttachDirection(Direction attachDirection) {
        this.attachDirection = attachDirection;
    }

    public Direction getAttachDirection() {
        return attachDirection;
    }

    public void incJumps() {
        this.jumps++;
    }

    public void setJumps(int jumps) {this.jumps = jumps;};

    /**
     * Returns true if tracker is at reset value
     */
    public boolean isReset() {
        return jumps == 0 && !isClimbing && !isSliding;
    }

    public void reset() {
        jumps = 0;
        isClimbing = false;
        isSliding = false;
    }

    public void sendPacketToPlayer(ServerPlayer player) {
        //ClimbingCapabilitySyncPacket packet = new ClimbingCapabilitySyncPacket(writeToNBT(new CompoundNBT()));
        //ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
