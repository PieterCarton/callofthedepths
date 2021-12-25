package pjut.callofthedepths.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.lwjgl.system.CallbackI;
import pjut.callofthedepths.common.registry.COTDEntityTypes;
import pjut.callofthedepths.common.registry.COTDItems;

public class TorchArrow extends AbstractArrow {
    public TorchArrow(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public TorchArrow(Level level, LivingEntity livingEntity) {
        super(COTDEntityTypes.TORCH_ARROW.get(), livingEntity, level);
    }

    public TorchArrow(Level level, double x, double y, double z) {
        super(COTDEntityTypes.TORCH_ARROW.get(), x, y, z, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(COTDItems.TORCH_ARROW.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        hitResult.getEntity().setSecondsOnFire(5);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        BlockPos hitPos = hitResult.getBlockPos();
        Direction hitDirection = hitResult.getDirection();
        BlockState blockState = this.level.getBlockState(hitPos);

        BlockState placeState = null;
        BlockPos placePos = hitPos.mutable().move(hitDirection);

        boolean placementFailed = false;

        if (hitDirection == Direction.DOWN) {
            placementFailed = true;
        } else if (hitDirection == Direction.UP) {
            placeState = Blocks.TORCH.defaultBlockState();
        } else {
            placeState = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, hitDirection);
        }

        boolean replaceable = this.getLevel().getBlockState(placePos).getMaterial().isReplaceable();
        boolean validSide = blockState.isFaceSturdy(this.getLevel(), placePos, hitDirection);

        if (!placementFailed && replaceable && validSide) {
            // TODO: fix light glitch on reload world
            level.setBlock(placePos, placeState, Integer.MAX_VALUE);
        } else {
            // TODO: fix multi-shot crossbow duplication
            ItemStack itemStack = new ItemStack(COTDItems.TORCH_ARROW.get());
            level.addFreshEntity(new ItemEntity(this.getLevel(), this.getX(), this.getY(), this.getZ(), itemStack));
        }

        this.discard();
    }
}
