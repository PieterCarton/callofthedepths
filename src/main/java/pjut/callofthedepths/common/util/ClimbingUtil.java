package pjut.callofthedepths.common.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import pjut.callofthedepths.common.item.ClimbingPickItem;

public class ClimbingUtil {
    // TODO: use as sole method of determining max jumps
    public static int getMaxJumps(Player p) {
        Item mainHandItem = p.getMainHandItem().getItem();
        Item offhandItem = p.getOffhandItem().getItem();

        int mainHandJumps = mainHandItem instanceof ClimbingPickItem c ? c.getMaxJumps() : 0;
        int offhandJumps = offhandItem instanceof ClimbingPickItem c ? c.getMaxJumps() : 0;

        return Math.max(mainHandJumps, offhandJumps);
    }
}
