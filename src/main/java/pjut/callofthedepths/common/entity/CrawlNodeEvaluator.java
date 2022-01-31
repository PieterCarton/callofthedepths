package pjut.callofthedepths.common.entity;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.AABB;

public class CrawlNodeEvaluator extends NodeEvaluator {
    private final Long2ObjectMap<BlockPathTypes> pathTypesByPosCache = new Long2ObjectOpenHashMap<>();

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

    public Target getGoal(double p_77550_, double p_77551_, double p_77552_) {
        return new Target(this.getNode(Mth.floor(p_77550_), Mth.floor(p_77551_), Mth.floor(p_77552_)));
    }

    @Override
    public int getNeighbors(Node[] neighbors, Node node) {
        int count = 0;
        return count;
    }

    private BlockPathTypes getBlockPathType(Mob p_77573_, BlockPos p_77574_) {
        return this.getCachedBlockType(p_77573_, p_77574_.getX(), p_77574_.getY(), p_77574_.getZ());
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter p_77337_, int p_77338_, int p_77339_, int p_77340_, Mob p_77341_, int p_77342_, int p_77343_, int p_77344_, boolean p_77345_, boolean p_77346_) {
        return null;
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter p_77333_, int p_77334_, int p_77335_, int p_77336_) {
        return null;
    }

    private boolean hasClimbableWall(BlockPos pos) {
        boolean climbable = false;
        for (BlockPos position: ImmutableSet.of(pos.above(), pos.east(), pos.west(), pos.north(), pos.south())) {
            climbable |= this.level.getBlockState(pos).isCollisionShapeFullBlock(this.level, pos);
        }
        return climbable;
    }

    private boolean hasPositiveMalus(BlockPos p_77647_) {
        return this.getBlockPathType(this.mob, p_77647_).getMalus() >= 0.0F;
    }

    protected BlockPathTypes getCachedBlockType(Mob p_77568_, int p_77569_, int p_77570_, int p_77571_) {
        return this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(p_77569_, p_77570_, p_77571_), (p_77566_) -> {
            return this.getBlockPathType(this.level, p_77569_, p_77570_, p_77571_, p_77568_, this.entityWidth, this.entityHeight, this.entityDepth, this.canOpenDoors(), this.canPassDoors());
        });
    }
}
