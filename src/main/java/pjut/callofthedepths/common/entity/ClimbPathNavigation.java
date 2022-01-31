package pjut.callofthedepths.common.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import pjut.callofthedepths.client.debug.DebugEventHandler;

public class ClimbPathNavigation extends GroundPathNavigation {
    public ClimbPathNavigation(Mob p_26448_, Level p_26449_) {
        super(p_26448_, p_26449_);
    }

    @Override
    public Path createPath(BlockPos p_26475_, int p_26476_) {
        Path path = super.createPath(p_26475_, p_26476_);
        if (path != null) {
            DebugEventHandler.addPath(this.mob.getId(), path);
        }

        return path;
    }
}
