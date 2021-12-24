package pjut.callofthedepths.common.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
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
}
