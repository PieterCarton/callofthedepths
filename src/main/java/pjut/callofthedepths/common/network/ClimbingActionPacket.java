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
    private Direction attachDirection;

    public ClimbingActionPacket(ClimbingAction action) {
        this(action, Direction.NORTH);
    }

    public ClimbingActionPacket(ClimbingAction action, Direction attachDirection) {
        this.action = action;
        this.attachDirection = attachDirection;
    }

    public ClimbingActionPacket(FriendlyByteBuf buffer) {
        this.action = buffer.readEnum(ClimbingAction.class);
        this.attachDirection = buffer.readEnum(Direction.class);
    }

    @Override
    public void handleServerside(NetworkEvent.Context ctx) {
        Player player = ctx.getSender();
        switch (action){
            case JUMP:
                ClimbingPickItem.onJump(player);
                break;
            case ATTACH:
                ClimbingPickItem.onAttach(player, this.attachDirection, Integer.MAX_VALUE);
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
        buffer.writeEnum(this.attachDirection);
    }
}
