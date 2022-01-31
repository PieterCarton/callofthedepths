package pjut.callofthedepths.common.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class DebugPathFinder extends PathFinder {
    public DebugPathFinder(NodeEvaluator p_77425_, int p_77426_) {
        super(p_77425_, p_77426_);
    }

    @Nullable
    @Override
    public Path findPath(PathNavigationRegion p_77428_, Mob p_77429_, Set<BlockPos> p_77430_, float p_77431_, int p_77432_, float p_77433_) {
        Path path = super.findPath(p_77428_, p_77429_, p_77430_, p_77431_, p_77432_, p_77433_);

        Minecraft.getInstance().debugRenderer.pathfindingRenderer.addPath(1, path, 64.0f);

        return path;
    }
}
