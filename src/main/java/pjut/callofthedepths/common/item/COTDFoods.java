package pjut.callofthedepths.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class COTDFoods {
    public static final FoodProperties GRANOLA_BAR = new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).build();
    public static final FoodProperties GLOW_BERRY_BAR = new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0), 1.0f).build();
}
