package pjut.callofthedepths.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TorchArrowItem extends ArrowItem {
    public TorchArrowItem(Properties p_40512_) {
        super(p_40512_);
    }

    public AbstractArrow createArrow(Level p_40513_, ItemStack p_40514_, LivingEntity p_40515_) {
        System.out.println("called");
        return new SpectralArrow(p_40513_, p_40515_);
    }
}
