package pjut.callofthedepths.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;

public class Crawler extends Monster {

    public static final int MIN_DROP_HEIGHT = 4;

    public Crawler(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new MoveControl(this);
        this.navigation = new ClimbPathNavigation(this, level);
    }



    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MoveAbovePlayerGoal(this, 32));
        //this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        //this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .build();
    }

    @Override
    public boolean onClimbable() {
        return this.horizontalCollision;
    }

    public class MoveAbovePlayerGoal extends Goal {

        private BlockPos playerPos;
        private BlockPos targetPos;

        private Player nearestPlayer;
        private PathfinderMob mob;
        private int maxHeight;

        public MoveAbovePlayerGoal(PathfinderMob mob, int maxHeight) {
            this.mob = mob;
            this.maxHeight = maxHeight;
        }

        @Override
        public boolean canUse() {
            nearestPlayer = this.mob.getLevel().getNearestPlayer(mob, 32);

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
            System.out.println("Target before: " + targetPos.toShortString());
            this.mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0D);
            /*
            Node end = this.mob.getNavigation().getPath().getEndNode();
            if (end != null) {
                System.out.println("Target after: " + end.toString());
            }
             */
        }

        @Override
        public void stop() {
            this.mob.getNavigation().stop();
        }
    }

    public class CrawlerMoveControl extends MoveControl {

        public CrawlerMoveControl(Mob p_24983_) {
            super(p_24983_);
        }

        @Override
        public void tick() {
            // if on ground: super code
            super.tick();

            // if climbing: custom code
        }
    }
}
