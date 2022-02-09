package pjut.callofthedepths.common.network;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import pjut.callofthedepths.common.item.ClimbingPickItem;
import pjut.callofthedepths.common.registry.COTDItems;

public class ClimbingActionPacket extends ClientToServerPacket {
    public enum ClimbingAction{
        ATTACH,
        RELEASE,
        JUMP,
        SLIDE,
        LAND
    }

    private ClimbingAction action;
    // TODO: what is the point of this?
    private double playerHeight;
    private Direction attachDirection;

    public ClimbingActionPacket(ClimbingAction action, double playerHeight) {
        this(action, playerHeight, Direction.NORTH);
    }

    public ClimbingActionPacket(ClimbingAction action, double playerHeight, Direction attachDirection) {
        this.action = action;
        this.playerHeight = playerHeight;
        this.attachDirection = attachDirection;
    }

    public ClimbingActionPacket(FriendlyByteBuf buffer) {
        this.action = buffer.readEnum(ClimbingAction.class);
        this.playerHeight = buffer.readDouble();
        this.attachDirection = buffer.readEnum(Direction.class);
    }

    @Override
    public void handleServerside(NetworkEvent.Context ctx) {
        ClimbingPickItem climbingPick = (ClimbingPickItem) COTDItems.CLIMBING_PICK.get();
        Player player = ctx.getSender();
        switch (action){
            case JUMP:
                ClimbingPickItem.onJump(player);
                break;
            case ATTACH:
                climbingPick.onAttach(player, this.attachDirection);
                break;
            case RELEASE:
                ClimbingPickItem.onRelease(player);
                break;
            case SLIDE:
                ClimbingPickItem.onStartSliding(player);
                break;
            case LAND:
                ClimbingPickItem.onLand(player);
        }
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeEnum(action);
        buffer.writeDouble(playerHeight);
        buffer.writeEnum(this.attachDirection);
    }
}
