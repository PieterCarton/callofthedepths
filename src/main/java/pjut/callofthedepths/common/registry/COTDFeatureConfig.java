package pjut.callofthedepths.common.registry;

import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class COTDFeatureConfig {
    public static final ConfiguredFeature<?, ?> POLE = FeatureUtils.register("pole", COTDFeatures.POLE.get().configured(FeatureConfiguration.NONE));

    public static void init(){};
}
