package pjut.callofthedepths.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import pjut.callofthedepths.common.registry.COTDBlocks;

import javax.annotation.Nullable;
import java.util.Random;

public class RopeBlock extends Block {
    private static final Property<Boolean> ATTACHED = BooleanProperty.create("attached");
    private static final Property<Boolean> TOP = BooleanProperty.create("top");

    private static final VoxelShape SHAPE_LONG = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape SHAPE_SHORT = Block.box(6.0D, 3.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    public RopeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ATTACHED, TOP);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        return aboveState.getMaterial().blocksMotion() || aboveState.is(COTDBlocks.ROPE_BLOCK.get());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        boolean isAttached = hasRopeBelow(world, pos);
        boolean isTop = !world.getBlockState(pos.above()).is(COTDBlocks.ROPE_BLOCK.get());

        return this.defaultBlockState().setValue(ATTACHED, isAttached).setValue(TOP, isTop);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            boolean ropeBelow = facingState.is(COTDBlocks.ROPE_BLOCK.get());
            if (!stateIn.getValue(ATTACHED) && ropeBelow) {
                return stateIn.setValue(ATTACHED, true);
            }
            if (stateIn.getValue(ATTACHED) && !ropeBelow) {
                return stateIn.setValue(ATTACHED, false);
            }
        } else if (facing == Direction.UP) {
            if (!level.isClientSide()) {
                level.scheduleTick(currentPos, this, 1);
            }
        }

        return stateIn;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random p_60465_) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    private boolean hasRopeBelow(BlockGetter level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState stateBelow = level.getBlockState(below);

        return stateBelow.is(COTDBlocks.ROPE_BLOCK.get());
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos p_60557_, CollisionContext p_60558_) {
        if (!state.getValue(ATTACHED)) {
            return SHAPE_SHORT;
        }
        return SHAPE_LONG;
    }
}
