package pjut.callofthedepths.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import pjut.callofthedepths.common.block.entity.CrockPotBlockEntity;
import pjut.callofthedepths.common.registry.COTDBlockEntities;
import pjut.callofthedepths.common.registry.COTDParticleTypes;

import java.util.Random;

public class CrockPotBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = box(2, 0, 2, 14, 5, 14);

    public CrockPotBlock(Properties of) {
        super(of);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrockPotBlockEntity(pos, state);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, Random random) {
        CrockPotBlockEntity crockPot = (CrockPotBlockEntity) level.getBlockEntity(pos);

        if (crockPot != null && crockPot.isHeated() && crockPot.isFull()) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 1d / 16d;
            double z = pos.getZ() + 0.5;

            double dx = (random.nextDouble() - 0.5) * (5d / 8d);
            double dz = (random.nextDouble() - 0.5) * (5d / 8d);

            level.addParticle(COTDParticleTypes.CROCK_POT_BUBBLE.get(), x + dx, y, z + dz, 0.0, 0.0, 0.0);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, COTDBlockEntities.CROCK_POT_BE, CrockPotBlockEntity::serverTick);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighbourState, LevelAccessor level, BlockPos pos, BlockPos updatedPos) {
        if (direction == Direction.DOWN) {
            CrockPotBlockEntity.checkHeated(level, pos, (CrockPotBlockEntity) level.getBlockEntity(pos));
        }
        return blockState;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        CrockPotBlockEntity entity = (CrockPotBlockEntity) level.getBlockEntity(blockPos);
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        return entity.interact(player.getItemInHand(hand), player, hand);
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean p_60519_) {
        Containers.dropContents(level, pos, (CrockPotBlockEntity) level.getBlockEntity(pos));
        super.onRemove(oldState, level, pos, newState, p_60519_);
    }
}
