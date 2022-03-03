package pjut.callofthedepths.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pjut.callofthedepths.common.registry.COTDBiomes;

import java.util.function.Consumer;


@Mixin(OverworldBiomeBuilder.class)
public abstract class MixinOverworldBiomeBuilder {
    @Shadow protected abstract void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187201_, Climate.Parameter p_187202_, Climate.Parameter p_187203_, Climate.Parameter p_187204_, Climate.Parameter p_187205_, Climate.Parameter p_187206_, float p_187207_, ResourceKey<Biome> p_187208_);

    @Inject(at = @At("HEAD"), method = "addBiomes(Ljava/util/function/Consumer;)V")
    private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, CallbackInfo info) {
        System.out.println("Hello from addBiomes!");
        this.addUndergroundBiome(consumer, Climate.Parameter.span(-1.0F, 1.0F), Climate.Parameter.span(-1.0F, 1.0F), Climate.Parameter.span(-1.0F, 1.0F), Climate.Parameter.span(-1.0F, 1.0F), Climate.Parameter.span(-1.0F, 1.0F), 0.0F, COTDBiomes.INFESTED_CAVERNS_KEY);
    }
}
