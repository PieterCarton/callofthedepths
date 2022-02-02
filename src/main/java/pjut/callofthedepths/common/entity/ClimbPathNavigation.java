package pjut.callofthedepths.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import pjut.callofthedepths.client.debug.DebugEventHandler;

import java.util.ArrayList;
import java.util.List;

public class ClimbPathNavigation extends PathNavigation {
    private Crawler crawler;
    private NodeEvaluator nodeEvaluator;

    public ClimbPathNavigation(Crawler mob, Level level) {
        super(mob, level);
        crawler = mob;
    }

    @Override
    protected PathFinder createPathFinder(int range) {
        this.nodeEvaluator = new ClimbNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, range);
    }

    // TODO: should be able to update while climbing as well
    protected boolean canUpdatePath() {
        return this.mob.isOnGround() || this.isInLiquid() || this.mob.isPassenger() || this.mob.getEntityData().get(Crawler.IS_CLIMBING);
    }

    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.mob.getY(), this.mob.getZ());
    }


    @Override
    public Path createPath(BlockPos p_26475_, int p_26476_) {
        // TODO: check destination: might not be climbable BlockPos

        Path path = super.createPath(p_26475_, p_26476_);

        if (path != null) {
            DebugEventHandler.addPath(this.mob.getId(), path);
        }

        return path;
    }

    @Override
    public void tick() {
        ++this.tick;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }

        if (!this.isDone()) {
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (this.path != null && !this.path.isDone()) {
                Vec3 vec3 = this.path.getNextEntityPos(this.mob);
                if (this.mob.getBlockX() == Mth.floor(vec3.x) && this.mob.getBlockY() == Mth.floor(vec3.y) && this.mob.getBlockZ() == Mth.floor(vec3.z)) {
                    this.path.advance();
                }
            }

            DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
            if (!this.isDone()) {
                Vec3 vec31 = this.path.getNextEntityPos(this.mob);
                this.mob.getMoveControl().setWantedPosition(vec31.x, vec31.y, vec31.z, this.speedModifier);

                if (this.path.getNextNode().type == BlockPathTypes.OPEN) {
                    this.mob.getEntityData().set(Crawler.IS_CLIMBING, true);
                    System.out.println("start climbing");

                    // get new targetOrientationDirection
                    Direction newTargetOrientation = getTargetOrientation(this.path.getPreviousNode().asBlockPos(),this.path.getNextNodePos(), level, this.crawler.getOrientation());
                    System.out.println(newTargetOrientation.toString());
                    this.crawler.setTargetClimbOrientation(newTargetOrientation);
                } else {
                    this.mob.getEntityData().set(Crawler.IS_CLIMBING, false);
                    System.out.println("stop climbing");
                }
            }
        }
    }

    protected static Direction getTargetOrientation(BlockPos current, BlockPos next, BlockGetter blockGetter, Direction currentOrientation) {
        List<Direction> validDirections = new ArrayList<>(6);

        BlockPos relativeDirection = current.subtract(next);
        Direction travelDirection = Direction.fromNormal(relativeDirection);

        for (Direction direction: Direction.values()) {
            // valid climb direction must be orthogonal to travelDirection
            // there must be a solid block in this direction from the next blockPos
            if (direction != travelDirection && direction != travelDirection.getOpposite()
                    && blockGetter.getBlockState(next.relative(direction)).getMaterial().isSolid()) {
                if (direction == currentOrientation) {
                    return direction;
                }
                validDirections.add(direction);
            }
        }

        if (validDirections.isEmpty()) {
            return null;
        }

        // should get closest to currentDirection in future
        return validDirections.get(0);
    }

    public void setCanOpenDoors(boolean p_26441_) {
        this.nodeEvaluator.setCanOpenDoors(p_26441_);
    }

    public boolean canPassDoors() {
        return this.nodeEvaluator.canPassDoors();
    }

    public void setCanPassDoors(boolean p_26444_) {
        this.nodeEvaluator.setCanPassDoors(p_26444_);
    }

    public boolean canOpenDoors() {
        return this.nodeEvaluator.canPassDoors();
    }
}
