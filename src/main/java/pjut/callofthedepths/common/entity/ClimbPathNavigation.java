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
import org.lwjgl.system.CallbackI;
import pjut.callofthedepths.client.debug.DebugEventHandler;

import java.util.ArrayList;
import java.util.List;

public class ClimbPathNavigation extends PathNavigation {
    private Crawler crawler;
    private NodeEvaluator nodeEvaluator;

    public ClimbPathNavigation(Crawler crawler, Level level) {
        super(crawler, level);
        this.crawler = crawler;
    }

    @Override
    protected PathFinder createPathFinder(int range) {
        this.nodeEvaluator = new ClimbNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, range);
    }

    protected boolean canUpdatePath() {
        return this.crawler.isOnGround() || this.isInLiquid() || this.crawler.isPassenger() || this.crawler.isClimbing();
    }

    protected Vec3 getTempMobPos() {
        return new Vec3(this.crawler.getX(), this.crawler.getY(), this.crawler.getZ());
    }


    @Override
    public Path createPath(BlockPos p_26475_, int p_26476_) {
        // TODO: check destination: might not be climbable BlockPos

        Path path = super.createPath(p_26475_, p_26476_);

        if (path != null) {
            DebugEventHandler.addPath(this.crawler.getId(), path);
        }

        return path;
    }

    // BLOCKED ?!?!?!
    @Override
    public void tick() {
        System.out.println(crawler.blockPosition());
        ++this.tick;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }

        if (!this.isDone()) {
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (this.path != null && !this.path.isDone()) {
                Vec3 vec3 = this.path.getNextEntityPos(this.crawler);
                if (this.crawler.getBlockX() == Mth.floor(vec3.x) && this.crawler.getBlockY() == Mth.floor(vec3.y) && this.crawler.getBlockZ() == Mth.floor(vec3.z)) {
                    this.path.advance();
                }
            }

            DebugPackets.sendPathFindingPacket(this.level, this.crawler, this.path, this.maxDistanceToWaypoint);
            if (!this.isDone()) {
                Vec3 nextPos = this.path.getNextEntityPos(this.crawler);
                // System.out.printf("Next entity pos: %s \n", nextPos.toString());
                // System.out.printf("Current Index: %s \n", this.path.getNextNodeIndex());
                //System.out.printf("Final pos: %s \n", this.path.getEndNode().toString());
                Vec3 wallAllignment = new Vec3(0, 0, 0);

                if (this.path.getNextNode().type == BlockPathTypes.OPEN || this.path.getNextNode().equals(this.path.getEndNode()) /*Do some check of blocks underneath here*/) {
                    this.crawler.setClimbing(true);
                    if (hasCeilingAbove(new BlockPos(nextPos))) {
                        wallAllignment.add(0.0, 0.5, 0.0);
                    }
                } else {
                    //this.crawler.setClimbing(false);
                }

                this.crawler.getMoveControl().setWantedPosition(nextPos.x, nextPos.y + wallAllignment.y(), nextPos.z, this.speedModifier);
            }
        }
    }

    @Override
    protected void followThePath() {
        super.followThePath();
    }

    private boolean hasCeilingAbove(BlockPos blockPos) {
        return level.getBlockState(blockPos.above()).getMaterial().isSolid();
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
