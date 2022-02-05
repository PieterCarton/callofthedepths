package pjut.callofthedepths.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import pjut.callofthedepths.common.item.ClimbingPickItem;
import pjut.callofthedepths.common.registry.COTDItems;

public class ClimbingActionPacket extends ClientToServerPacket {
    public enum ClimbingAction{
        ATTACH,
        RELEASE,
        LEAP,
        SLIDE
    }

    private ClimbingAction action;
    private double playerHeight;

    public ClimbingActionPacket(ClimbingAction action, double playerHeight) {
        this.action = action;
        this.playerHeight = playerHeight;
    }

    public ClimbingActionPacket(FriendlyByteBuf buffer) {
        this.action = buffer.readEnum(ClimbingAction.class);
        this.playerHeight = buffer.readDouble();
    }

    // TODO: re-enable once ported
    @Override
    public void handleServerside(NetworkEvent.Context ctx) {
        ClimbingPickItem climbingPick = (ClimbingPickItem) COTDItems.CLIMBING_PICK.get();
        Player player = ctx.getSender();
        switch (action){
            case LEAP:
                //climbingPick.onLeap(player);
                break;
            case ATTACH:
                //climbingPick.onAttach(player);
                break;
            case RELEASE:
                //climbingPick.onRelease(player);
                break;
            case SLIDE:
                //climbingPick.onStartSliding(player);
        }
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeEnum(action);
        buffer.writeDouble(playerHeight);
    }
}
