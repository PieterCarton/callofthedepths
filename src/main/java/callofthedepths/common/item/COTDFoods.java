package callofthedepths.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class COTDFoods {
    public static final FoodProperties GLOW_BERRY_BAR = new FoodProperties.Builder().nutrition(6).saturationMod(0.6f).alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0), 1.0f).build();
}
