package pjut.callofthedepths.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RopeItem extends BlockItem {

    public RopeItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        if (context.isSecondaryUseActive()) {
            return context;
        }

        Direction faceDirection = context.getClickedFace().getOpposite();
        BlockPos.MutableBlockPos mutable = context.getClickedPos().mutable().move(faceDirection);
        Level level = context.getLevel();
        Block ropeBlock = this.getBlock();
        BlockState blockState = level.getBlockState(mutable);

        if (blockState.is(ropeBlock)) {
            mutable.move(Direction.DOWN);
            while (level.getBlockState(mutable).is(ropeBlock)) {
                mutable.move(Direction.DOWN);
            }

            if (Level.isInSpawnableBounds(mutable) && level.getBlockState(mutable).canBeReplaced(context)) {
                return BlockPlaceContext.at(context, mutable, Direction.UP);
            }
            return null;
        }
        return context;
    }
}
