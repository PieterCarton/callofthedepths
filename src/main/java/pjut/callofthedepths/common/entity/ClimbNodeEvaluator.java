package pjut.callofthedepths.common.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
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
        } else if (this.mob.isOnGround() || this.hasClimbableWall(blockpos$mutableblockpos)) {
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

    private boolean hasClimbableWall(BlockPos pos) {
        boolean climbable = false;
        for (BlockPos position: ImmutableSet.of(pos.above(), pos.east(), pos.west(), pos.north(), pos.south())) {
            climbable |= this.level.getBlockState(pos).isCollisionShapeFullBlock(this.level, pos);
        }
        return climbable;
    }

    @Override
    public int getNeighbors(Node[] nodes, Node node) {
        int i = super.getNeighbors(nodes, node);

        nodes[i++] = this.getNode(node.x, node.y + 1, node.z);

        return i;
    }
}
