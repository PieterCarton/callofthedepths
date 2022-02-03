package pjut.callofthedepths.common.item;

import com.mojang.math.Vector3d;
import net.minecraft.client.particle.BlockMarker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;

public class ClimbingPickItem extends PickaxeItem {

    private static final double CLING_DISTANCE = 0.1;
    private static final int USAGE_COOLDOWN = 5;

    private int currentCooldown = 0;
    private final int maxLeaps;

    public ClimbingPickItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder, int maxLeaps) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
        this.maxLeaps = maxLeaps;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        InteractionHand hand = useOnContext.getHand();

        if (!player.level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) player.level;
            Vec3 particlePos = player.position();
            BlockState clickedState = level.getBlockState(useOnContext.getClickedPos());
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, clickedState), particlePos.x(), particlePos.y(), particlePos.z(), 10, 0.0, 0.0, 0.0, 0.3);
        }
        if (level.isClientSide() && currentCooldown < 0 && canClimbOnWall(player)) {
            currentCooldown = USAGE_COOLDOWN;
            if (player.isNoGravity() == true) {
                //onRelease(playerIn);
            } else {
                player.swing(hand);
                level.playSound(player, player.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0f, 1.0f);
                //onAttach(playerIn);
            }
        }

        return super.useOn(useOnContext);
    }

    /*
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        System.out.println("use");
        if (!player.level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) player.level;
            Vec3 particlePos = player.position();
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()), particlePos.x(), particlePos.y(), particlePos.z(), 10, 0.0, 0.0, 0.0, 0.3);
        }
        if (level.isClientSide() && currentCooldown < 0 && canClimbOnWall(player)) {
            currentCooldown = USAGE_COOLDOWN;
            if (player.isNoGravity() == true) {
                //onRelease(playerIn);
            } else {
                player.swing(hand);
                level.playSound(player, player.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0f, 1.0f);
                //onAttach(playerIn);
            }
        }

        return super.use(level, player, hand);
    }
     */

    @Override
    public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) {
        super.onUseTick(p_41428_, p_41429_, p_41430_, p_41431_);
        System.out.println("use tick");
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        super.onUsingTick(stack, player, count);
        System.out.println("using tick");
    }

    private boolean canClimbOnWall(Player player) {
        Direction lookDirection = getLookDirection(player.getLookAngle());
        Vec3i lookDirectionVec = lookDirection.getNormal();

        // track block in some way
        BlockPos upperLookBlockPos = player.blockPosition().subtract(lookDirection.getNormal());
        BlockPos lowerLookBlockPos = upperLookBlockPos.offset(0, -1, 0);

        Level level = player.getLevel();
        if (level.getBlockState(upperLookBlockPos).getMaterial().isSolid() || level.getBlockState(lowerLookBlockPos).getMaterial().isSolid()) {
            // position of player relative to center of block
            Vec3 relativePosition = player.position().subtract(player.position().x() + 0.5,0 ,player.position().z() + 0.5);
            double distanceFromCentre = relativePosition.dot(new Vec3(lookDirectionVec.getX(), 0, lookDirectionVec.getZ()));
            if (distanceFromCentre > CLING_DISTANCE) {
                return true;
            }

        }

        return false;
    }

    private Direction getLookDirection(Vec3 lookVec) {
        Vec3 absLookVec = new Vec3(Math.abs(lookVec.x), Math.abs(lookVec.y), Math.abs(lookVec.z));

        if (absLookVec.x >= absLookVec.z) {
            if (lookVec.x > 0) {
                return Direction.EAST;
            } else {
                return Direction.WEST;
            }
        } else {
            if (lookVec.z > 0) {
                return Direction.SOUTH;
            } else {
                return Direction.NORTH;
            }
        }
    }
}
