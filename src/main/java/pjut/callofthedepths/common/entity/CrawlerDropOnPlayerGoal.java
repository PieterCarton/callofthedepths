package pjut.callofthedepths.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class CrawlerDropOnPlayerGoal extends Goal {

    private Crawler crawler;

    public CrawlerDropOnPlayerGoal(Crawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        return this.crawler.isClimbing() && canFallOnPlayer(this.crawler.getTarget());
    }

    private boolean canFallOnPlayer(LivingEntity player) {
        if (player == null) {
            return false;
        }

        BlockPos crawlerPos = this.crawler.blockPosition();
        BlockPos playerPos = player.blockPosition();

        // improve above detection later
        return playerPos.getX() == crawlerPos.getX() && playerPos.getZ() == crawlerPos.getZ();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.crawler.isOnGround();
    }

    @Override
    public void start() {
        this.crawler.getNavigation().stop();
        this.crawler.setClimbing(false);
        System.out.println("used fall goal");

    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void tick() {
        System.out.println("tick");
        this.crawler.resetFallDistance();
    }


}
