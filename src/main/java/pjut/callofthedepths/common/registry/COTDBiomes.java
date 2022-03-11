package pjut.callofthedepths.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pjut.callofthedepths.common.CallOfTheDepths;

import static net.minecraftforge.common.BiomeDictionary.Type.DENSE;
import static net.minecraftforge.common.BiomeDictionary.Type.HOT;
import static net.minecraftforge.common.BiomeDictionary.Type.JUNGLE;
import static net.minecraftforge.common.BiomeDictionary.Type.OVERWORLD;
import static net.minecraftforge.common.BiomeDictionary.Type.WET;

public class COTDBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, CallOfTheDepths.MOD_ID);

    public static final ResourceKey<Biome> INFESTED_CAVERNS_KEY = registerKey("infested_caverns");

    public static final RegistryObject<Biome> INFESTED_CAVERNS = registerBiome("infested_caverns");

    private static ResourceKey<Biome> registerKey(String name) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(CallOfTheDepths.MOD_ID, name));
    }

    private static RegistryObject<Biome> registerBiome(String name) {
        //return BIOMES.register(name, () -> OverworldBiomes.jungle());
        return BIOMES.register(name, () ->OverworldBiomes.lushCaves());
        /*return BIOMES.register(name, () -> new Biome.BiomeBuilder()
                .biomeCategory(Biome.BiomeCategory.UNDERGROUND)
                .downfall(0.0f)
                .precipitation(Biome.Precipitation.NONE)
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(new BiomeGenerationSettings.Builder().addCarver(GenerationStep.Carving.AIR, Carvers.CAVE).build())
                .temperature(1.0f)
                .specialEffects(defaultSpecialEffect())
                .build());*/
    }

    public static BiomeSpecialEffects defaultSpecialEffect() {
        return new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(4159204).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(null).build();
    }

    public static void registerBiomes() {
        System.out.println("Called register biomes");
        //BiomeDictionary.addTypes(INFESTED_CAVERNS_KEY, BiomeDictionary.Type.PLATEAU, BiomeDictionary.Type.OVERWORLD, BiomeDictionary.Type.HOT);
        BiomeManager.addAdditionalOverworldBiomes(INFESTED_CAVERNS_KEY);
        BiomeDictionary.addTypes(INFESTED_CAVERNS_KEY, HOT, WET, DENSE, JUNGLE, OVERWORLD);
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(INFESTED_CAVERNS_KEY, 30));
    }

    public static void init() {
        BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
