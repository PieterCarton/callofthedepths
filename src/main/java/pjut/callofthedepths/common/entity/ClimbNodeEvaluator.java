package pjut.callofthedepths.common.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;

public class ClimbNodeEvaluator extends WalkNodeEvaluator {

    @Override
    public Target getGoal(double x, double y, double z) {
        return super.getGoal(x, y, z);
    }

    // NOTE: if node is blocked, it should be traversed by climbing
    @Override
    public Node getStart() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = this.mob.getBlockY();
        BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), (double)i, this.mob.getZ()));
        if (this.canFloat() && this.mob.isInWater()) {
            while (true) {
                if (!blockstate.is(Blocks.WATER) && blockstate.getFluidState() != Fluids.WATER.getSource(false)) {
                    --i;
                    break;
                }

                ++i;
                blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), (double) i, this.mob.getZ()));
            }
        } else if (this.mob.isOnGround() || this.hasWallToSide(blockpos$mutableblockpos)) {
            i = Mth.floor(this.mob.getY() + 0.5D);
        } else {
            BlockPos blockpos;
            for (blockpos = this.mob.blockPosition(); (this.level.getBlockState(blockpos).isAir() || this.level.getBlockState(blockpos).isPathfindable(this.level, blockpos, PathComputationType.LAND)) && blockpos.getY() > this.mob.level.getMinBuildHeight(); blockpos = blockpos.below()) {
            }

            i = blockpos.above().getY();
        }

        BlockPos blockpos1 = this.mob.blockPosition();
        BlockPathTypes blockpathtypes = this.getCachedBlockType(this.mob, blockpos1.getX(), i, blockpos1.getZ());
        if (this.mob.getPathfindingMalus(blockpathtypes) < 0.0F) {
            AABB aabb = this.mob.getBoundingBox();
            if (this.hasPositiveMalus(blockpos$mutableblockpos.set(aabb.minX, (double)i, aabb.minZ)) || this.hasPositiveMalus(blockpos$mutableblockpos.set(aabb.minX, (double)i, aabb.maxZ)) || this.hasPositiveMalus(blockpos$mutableblockpos.set(aabb.maxX, (double)i, aabb.minZ)) || this.hasPositiveMalus(blockpos$mutableblockpos.set(aabb.maxX, (double)i, aabb.maxZ))) {
                Node node = this.getNode(blockpos$mutableblockpos);
                node.type = this.getBlockPathType(this.mob, node.asBlockPos());
                node.costMalus = this.mob.getPathfindingMalus(node.type);
                return node;
            }
        }

        Node node1 = this.getNode(blockpos1.getX(), i, blockpos1.getZ());
        node1.type = this.getBlockPathType(this.mob, node1.asBlockPos());
        node1.costMalus = this.mob.getPathfindingMalus(node1.type);
        return node1;
    }

    private boolean hasPositiveMalus(BlockPos p_77647_) {
        return this.getBlockPathType(this.mob, p_77647_).getMalus() >= 0.0F;
    }

    private BlockPathTypes getBlockPathType(Mob p_77573_, BlockPos p_77574_) {
        return this.getCachedBlockType(p_77573_, p_77574_.getX(), p_77574_.getY(), p_77574_.getZ());
    }

    @Override
    public int getNeighbors(Node[] nodes, Node node) {
        int i = super.getNeighbors(nodes, node);

        Node above = this.getNode(node.asBlockPos().above());
        Direction wallDirection = getClimbableWallDirection(above);
        if (!above.closed && hasWallToSide(above.asBlockPos())) {
            nodes[i++] = above;
        }

        // add nodes in horizontal direction iff they do not have ground beneath
        // north
        Node north = this.getNode(node.asBlockPos().north());
        if (!north.closed && hasCeilingAbove(north.asBlockPos()) && !hasGroundUnderneath(north.asBlockPos())) {
            nodes[i++] = north;
        }

        // south
        Node south = this.getNode(node.asBlockPos().south());
        if (!south.closed && hasCeilingAbove(south.asBlockPos()) && !hasGroundUnderneath(south.asBlockPos())) {
            nodes[i++] = this.getNode(node.x, node.y, node.z + 1);
        }

        // east
        Node east = this.getNode(node.asBlockPos().east());
        if (!east.closed && hasCeilingAbove(east.asBlockPos()) && !hasGroundUnderneath(east.asBlockPos())) {
            nodes[i++] = this.getNode(node.x + 1, node.y, node.z);
        }

        // west
        Node west = this.getNode(node.asBlockPos().west());
        if (!west.closed && hasCeilingAbove(west.asBlockPos()) && !hasGroundUnderneath(west.asBlockPos())) {
            nodes[i++] = this.getNode(node.x - 1, node.y, node.z);
        }

        return i;
    }

    private Direction getClimbableWallDirection(Node node) {
        BlockPos nodePos = node.asBlockPos();

        // must not be inside block
        if (this.level.getBlockState(nodePos).getMaterial().isSolid()) {
            return null;
        }

        for (Direction direction: ImmutableList.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)) {
            BlockPos position = nodePos.relative(direction);
            if (this.level.getBlockState(position).getMaterial().isSolid()) {
                return direction;
            }
        }
        return null;
    }

    private boolean hasWallToSide(BlockPos pos) {
        boolean climbable = false;

        // must not be inside block
        if (this.level.getBlockState(pos).getMaterial().isSolid()) {
            return false;
        }

        for (BlockPos position: ImmutableSet.of(pos.above(), pos.east(), pos.west(), pos.north(), pos.south())) {
            climbable |= this.level.getBlockState(position).getMaterial().isSolid();
        }
        return climbable;
    }

    private boolean hasGroundUnderneath(BlockPos blockPos) {
        return level.getBlockState(blockPos.below()).getMaterial().isSolid();
    }

    private boolean hasCeilingAbove(BlockPos blockPos) {
        return level.getBlockState(blockPos.above()).getMaterial().isSolid();
    }
}
