package pjut.callofthedepths.common.registry;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pjut.callofthedepths.common.level.feature.TestFeature;
import pjut.callofthedepths.common.level.feature.WebPatchFeature;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

public class COTDFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CallOfTheDepths.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> POLE = FEATURES.register("pole", () -> new TestFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> WEB_PATCH = FEATURES.register("web_patch", () -> new WebPatchFeature(NoneFeatureConfiguration.CODEC));

    public static void init() {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
