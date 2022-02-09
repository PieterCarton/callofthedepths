package pjut.callofthedepths.common.item;

import net.minecraft.client.Minecraft;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import pjut.callofthedepths.common.capability.ClimbingTracker;
import pjut.callofthedepths.common.capability.ClimbingTrackerCapability;
import pjut.callofthedepths.common.network.COTDPacketHandler;
import pjut.callofthedepths.common.network.ClimbingActionPacket;

import java.util.function.Consumer;

public class ClimbingPickItem extends PickaxeItem {

    private static final AttributeModifier SLIDE_FALL_SPEED_REDUCTION = new AttributeModifier("Slide fall-speed reduction", -0.06, AttributeModifier.Operation.ADDITION);

    private static final double CLING_DISTANCE = 0.1;
    private static final int USAGE_COOLDOWN = 5;

    private int currentCooldown = 0;
    private final int MAX_JUMPS;

    public ClimbingPickItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder, int maxJumps) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
        this.MAX_JUMPS = maxJumps;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (stack.getDamageValue() == stack.getMaxDamage() - 1) {
            onRelease((Player)entity);
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        onRelease(player);
        return super.onDroppedByPlayer(item, player);
    }

    // TODO activate on any right click
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
            if (player.isNoGravity()) {
                onRelease(player);
            } else {
                player.swing(hand);
                // TODO: fix sound
                level.playSound(player, player.blockPosition(), SoundEvents.ANVIL_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
                onAttach(player, getLookDirection(player.getLookAngle()));
            }
        }

        return super.useOn(useOnContext);
    }



    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!(entityIn instanceof Player player))
            return;

        if (!isHoldingClimbingPick(player)) {
            onRelease(player);
        }

        if (!isSelected)
            return;

        LazyOptional<ClimbingTracker> climbingCapability = ClimbingTrackerCapability.get(player);
        ClimbingTracker cap = climbingCapability.orElse(new ClimbingTracker());

        // To stay attached to the wall, the player must:
        // - have a solid wall in the direction they are attached
        // - must not be moving horizontally
        Vec3 movement = entityIn.getDeltaMovement();
        // TODO: also cancel sliding
        if (cap.isClimbing() && (!hasSolidWall(player, cap.getAttachDirection()) || movement.x() != 0.0 || movement.z() != 0.0)) {
            onRelease(player);
        }

        if (cap.isSliding()) {
            doWallSlide(level, player, cap);
        }

        // check for player initiating a wall slide or a new jump
        if (level.isClientSide && cap.isClimbing()) {
            if (Minecraft.getInstance().options.keyJump.isDown()) {
                onJump(player);
            } else if (player.isCrouching() && !cap.isSliding()) {
                onStartSliding(player);
            }
        }

        currentCooldown--;
        super.inventoryTick(stack, level, entityIn, itemSlot, isSelected);
    }

    private static boolean isHoldingClimbingPick(Player player) {
        return player.getMainHandItem().getItem() instanceof ClimbingPickItem || player.getOffhandItem().getItem() instanceof ClimbingPickItem;
    }

    private void doWallSlide(Level level, Player player, ClimbingTracker cap) {
        player.fallDistance = 0.0f;
        if (!player.isCrouching() && cap.getStableHeight() >= player.getY() && level.isClientSide) {
            // attach once again when stable height has been reached
            onAttach(player, cap.getAttachDirection());
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
            Vec3 relativePosition = player.position().subtract(player.blockPosition().getX() + 0.5,0 ,player.blockPosition().getZ() + 0.5);
            double distanceFromCentre = relativePosition.dot(new Vec3(lookDirectionVec.getX(), 0, lookDirectionVec.getZ()));
            return distanceFromCentre > CLING_DISTANCE;
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


    // Executed when player tries jump while climbing
    public static void onJump(Player player) {
        // switch gravity on
        player.setNoGravity(false);

        //update capability
        ClimbingTrackerCapability.get(player).ifPresent(cap -> {
            cap.incJumps();
            cap.setClimbing(false);
        });

        if (player.level.isClientSide) {
            // if on client, handle movement and inform server of action
            player.jumpFromGround();
            COTDPacketHandler.INSTANCE.sendToServer(new ClimbingActionPacket(ClimbingActionPacket.ClimbingAction.JUMP, player.getY()));
        }
    }

     // Executed when player tries to attach to wall
    public void onAttach(Player player, Direction attachDirection) {

        ClimbingTracker cap = ClimbingTrackerCapability.get(player).orElse(new ClimbingTracker());

        // if player is no longer allowed to attach, start sliding
        if (cap.getJumps() >= MAX_JUMPS && cap.getStableHeight() < player.getY()) {
            onStartSliding(player);
            return;
        }

        // stop sliding, start climbing
        removeSlidingModifier(player);
        cap.setSliding(false);
        cap.setClimbing(true);
        cap.setAttachDirection(attachDirection);

        // switch gravity off and update capability
        player.setNoGravity(true);
        player.fallDistance = 0.0f;
        cap.setStableHeight(player.getY());

        if (player.level.isClientSide) {
            // if on client, handle movement and inform server of action
            player.setDeltaMovement(Vec3.ZERO);
            COTDPacketHandler.INSTANCE.sendToServer(new ClimbingActionPacket(ClimbingActionPacket.ClimbingAction.ATTACH, player.getY()));
        }
    }

    // Executed when player stops climbing
    public static void onRelease(Player player) {
        ClimbingTrackerCapability.get(player).ifPresent(cap -> cap.setSliding(false));
        player.setNoGravity(false);
        removeSlidingModifier(player);

        if (player.level.isClientSide) {
            // if on client, inform server of action
            COTDPacketHandler.INSTANCE.sendToServer(new ClimbingActionPacket(ClimbingActionPacket.ClimbingAction.RELEASE, player.getY()));
        }
    }

    // TODO: behaviour for water??
    // Executed when player lands on solid ground
    public static void onLand(Player player) {
        // reset number of jumps, stop sliding
        ClimbingTrackerCapability.get(player)
                .ifPresent(ClimbingTracker::reset);

        player.setNoGravity(false);
        removeSlidingModifier(player);

        if (player.level.isClientSide) {
            COTDPacketHandler.INSTANCE.sendToServer(new ClimbingActionPacket(ClimbingActionPacket.ClimbingAction.LAND, player.getY()));
        }
    }

    public static void onStartSliding(Player player) {

        ClimbingTracker cap = ClimbingTrackerCapability.get(player).orElse(new ClimbingTracker());

        if (cap.isSliding()) {
            return;
        }

        cap.setSliding(true);
        addSlidingModifier(player);
        player.setNoGravity(false);

        // if on client, handle movement and inform server of action
        if (player.level.isClientSide) {
            player.setDeltaMovement(Vec3.ZERO);
            COTDPacketHandler.INSTANCE.sendToServer(new ClimbingActionPacket(ClimbingActionPacket.ClimbingAction.SLIDE, player.getY()));
        }

    }

    public static void addSlidingModifier(Player player) {
        // apply sliding attribute modifier, if not already present
        AttributeInstance attributeInstance = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        if (!attributeInstance.hasModifier(SLIDE_FALL_SPEED_REDUCTION)) {
            attributeInstance.addTransientModifier(SLIDE_FALL_SPEED_REDUCTION);
        }
    }

    public static void removeSlidingModifier(Player player) {
        // remove sliding attribute modifier, if present
        AttributeInstance attributeInstance = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        if (attributeInstance.hasModifier(SLIDE_FALL_SPEED_REDUCTION)) {
            attributeInstance.removeModifier(SLIDE_FALL_SPEED_REDUCTION);
        }
    }
}
