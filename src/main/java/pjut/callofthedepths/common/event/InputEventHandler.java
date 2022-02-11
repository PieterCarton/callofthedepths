package pjut.callofthedepths.common.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import pjut.callofthedepths.common.capability.ClimbingTrackerCapability;
import pjut.callofthedepths.common.item.ClimbingPickItem;

@Mod.EventBusSubscriber
public class InputEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent evt) {
        if (evt.side != LogicalSide.CLIENT) {
            return;
        }
        Player player = evt.player;
        ClimbingTrackerCapability.get(player).ifPresent(cap -> {
            if (player.isOnGround() && !cap.isReset()) {
                System.out.println("land triggered");
                ClimbingPickItem.onLand(player);
            }
        });
    }
}
