package pjut.callofthedepths.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Crawler extends Monster {
    private static final EntityDataAccessor<Boolean> IS_CLIMBING = SynchedEntityData.defineId(Crawler.class, EntityDataSerializers.BOOLEAN);

    public static final int MIN_DROP_HEIGHT = 4;

    public Crawler(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        //this.moveControl = new MoveControl(this);
        //this.moveControl = new CrawlerMoveControl(this, 10, true);
        //this.navigation = new ClimbPathNavigation(this, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.navigation = new FlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CrawlerDropOnPlayerGoal(this));
        this.goalSelector.addGoal(2, new MoveAbovePlayerGoal(this, 32));

        // use for target selection
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FLYING_SPEED, 0.23F)
                .build();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CLIMBING, false);
    }

    public void setClimbing(boolean isClimbing) {
        this.entityData.set(IS_CLIMBING, isClimbing);
    }

    public boolean isClimbing() {
        return this.entityData.get(IS_CLIMBING);
    }


    // Crawler does not seem to not be able to reuse goal
    public class MoveAbovePlayerGoal extends Goal {

        private BlockPos playerPos;
        private BlockPos targetPos;

        private LivingEntity nearestPlayer;
        private PathfinderMob mob;
        private int maxHeight;

        public MoveAbovePlayerGoal(PathfinderMob mob, int maxHeight) {
            this.mob = mob;
            this.maxHeight = maxHeight;
        }

        @Override
        public boolean canUse() {
            nearestPlayer = this.mob.getTarget();

            if (nearestPlayer != null) {
                BlockPos.MutableBlockPos mutableBlockPos = nearestPlayer.blockPosition().mutable();

                mutableBlockPos.move(Direction.UP, MIN_DROP_HEIGHT);

                for (int i = 0; i < this.maxHeight; i++) {
                    BlockState blockAtPos = this.mob.getLevel().getBlockState(mutableBlockPos);

                    if (blockAtPos.isFaceSturdy(this.mob.getLevel(), mutableBlockPos, Direction.DOWN)) {
                        this.targetPos = mutableBlockPos.below();
                        return true;
                    }

                    mutableBlockPos.move(Direction.UP);
                }
            }

            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return canUse();
        }

        @Override
        public void tick() {
            start();
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 1.0D);
        }

        @Override
        public void stop() {
            this.mob.getNavigation().stop();
        }
    }

    public class CrawlerMoveControl extends MoveControl {
        private final int maxTurn;
        private final boolean hoversInPlace;

        public CrawlerMoveControl(Mob mob, int maxTurn, boolean hoversInPlace) {
            super(mob);
            this.maxTurn = maxTurn;
            this.hoversInPlace = hoversInPlace;
        }

        @Override
        public void tick() {
            if (this.mob.getEntityData().get(IS_CLIMBING)) {
                climbTick();
            } else {
                this.mob.setNoGravity(false);
                super.tick();
            }

        }

        public void climbTick() {
            // priority 1: move against wall mob is climbing on
            // priority 2: move to next goal along wall

            if (this.operation == MoveControl.Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;
                this.mob.setNoGravity(true);
                double d0 = this.wantedX - this.mob.getX();
                double d1 = this.wantedY - this.mob.getY();
                double d2 = this.wantedZ - this.mob.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double)2.5000003E-7F) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }

                float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
                float f1;
                if (this.mob.isOnGround()) {
                    f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                } else {
                    f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
                }

                this.mob.setSpeed(f1);
                double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                if (Math.abs(d1) > (double)1.0E-5F || Math.abs(d4) > (double)1.0E-5F) {
                    float f2 = (float)(-(Mth.atan2(d1, d4) * (double)(180F / (float)Math.PI)));
                    this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f2, (float)this.maxTurn));
                    this.mob.setYya(d1 > 0.0D ? f1 : -f1);
                }
            } else {
                if (!this.hoversInPlace) {
                    this.mob.setNoGravity(false);
                }

                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
            }

        }
    }
}
