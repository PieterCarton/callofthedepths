package pjut.callofthedepths.common.registry;

import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

@Mod.EventBusSubscriber(modid = CallOfTheDepths.MOD_ID)
public class COTDFeaturePlacement {

    public static PlacedFeature POLE;

    public static void register(final FMLCommonSetupEvent evt) {
        System.out.println("PLACED FEATURES REGISTERED!!>!>!>");
        COTDFeatureConfig.init();
        POLE = PlacementUtils.register("pole", COTDFeatureConfig.POLE
                .placed(NoiseThresholdCountPlacement.of(-0.8D, 15, 4), RarityFilter.onAverageOnceEvery(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
    }

    @SubscribeEvent
    public static void addBiomeFeatures(BiomeLoadingEvent evt) {
        System.out.println("added features to: " + evt.getName());
        evt.getGeneration().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, POLE);
    }
}
