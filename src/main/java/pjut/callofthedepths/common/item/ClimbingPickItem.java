package pjut.callofthedepths.common.item;

import com.mojang.math.Vector3d;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.entity.Entity;
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
import net.minecraftforge.common.util.LazyOptional;
import pjut.callofthedepths.common.capability.ClimbingTracker;
import pjut.callofthedepths.common.capability.ClimbingTrackerCapability;

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
            // TODO: fix particle position + better particle
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, clickedState), particlePos.x(), particlePos.y(), particlePos.z(), 10, 0.0, 0.0, 0.0, 0.3);
        }
        if (level.isClientSide() && currentCooldown < 0 && canClimbOnWall(player)) {
            currentCooldown = USAGE_COOLDOWN;
            if (player.isNoGravity() == true) {
                //onRelease(playerIn);
            } else {
                player.swing(hand);
                // TODO: fix sound
                level.playSound(player, player.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0f, 1.0f);
                onAttach(playerIn);
            }
        }

        return super.useOn(useOnContext);
    }



    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!(entityIn instanceof Player)) {
            return;
        }

        Player player = (Player) entityIn;
        LazyOptional<ClimbingTracker> climbingCapability = ClimbingTrackerCapability.get(player);
        ClimbingTracker cap = climbingCapability.orElse(new ClimbingTracker());

        // To stay attached to the wall, the player must:
        // - have a solid wall in the direction they are attached
        // - must not be moving horizontally
        Vec3 movement = entityIn.getDeltaMovement();
        if (cap.isClimbing() && (!hasSolidWall(player, cap.getAttachDirection()) || movement.x() != 0.0 || movement.z() != 0.0)) {
            onRelease(player);
        }

        // check if player just landed on the ground
        if (cap.isClimbing() && player.isOnGround()) {
            onLand(player);
        }

        if (cap.isSliding()) {
            doWallSlide(level, player, cap);
        }

        // check for player initiating a wall slide or a new jump
        if (level.isClientSide && cap.isClimbing()) {
            if (Minecraft.getInstance().options.keyJump.isDown()) {
                System.out.println("leap");
                onJump(player);
            } else if (player.isCrouching() && !cap.isSliding()) {
                onStartSliding(player);
            }
        }

        currentCooldown--;
        super.inventoryTick(stack, level, entityIn, itemSlot, isSelected);
    }

    private void doWallSlide(Level level, Player player, ClimbingTracker cap) {
        player.fallDistance = 0.0f;
        if (!player.isCrouching() && cap.getStableHeight() >= player.getY() && level.isClientSide) {
            // attach once again when stable height has been reached
            onAttach(player);
        }

        if(player.tickCount % 3 == 0) {
            // TODO: better sounds and particle effects
            level.playSound(player, player.blockPosition(), SoundEvents.SOUL_SAND_STEP, SoundSource.PLAYERS, 1.0f, 1.0f);
            if (!level.isClientSide) {
                Vec3 particlePos = player.position();
                ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()), particlePos.x(), particlePos.y, particlePos.z(), 10, 0.0, 0.0, 0.0, 0.3);
            }
        }
    }

    private boolean canClimbOnWall(Player player) {
        Direction lookDirection = getLookDirection(player.getLookAngle());
        Vec3i lookDirectionVec = lookDirection.getNormal();

        if (hasSolidWall(player, lookDirection)) {
            // position of player relative to center of block
            Vec3 relativePosition = player.position().subtract(player.position().x() + 0.5,0 ,player.position().z() + 0.5);
            double distanceFromCentre = relativePosition.dot(new Vec3(lookDirectionVec.getX(), 0, lookDirectionVec.getZ()));

            if (distanceFromCentre > CLING_DISTANCE) {
                return true;
            }
        }

        return false;
    }

    private boolean hasSolidWall(Player player, Direction direction) {
        BlockPos upperLookBlockPos = player.blockPosition().offset(direction.getNormal());
        BlockPos lowerLookBlockPos = upperLookBlockPos.below();

        Level level = player.getLevel();
        return level.getBlockState(upperLookBlockPos).getMaterial().isSolid() || level.getBlockState(lowerLookBlockPos).getMaterial().isSolid();
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
