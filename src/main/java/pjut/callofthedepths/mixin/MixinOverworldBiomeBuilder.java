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
public class MixinOverworldBiomeBuilder {
    @Inject(at = @At("HEAD"), method = "addBiomes(Ljava/util/function/Consumer;)V")
    private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, CallbackInfo info) {
        System.out.println("Hello from addBiomes!");
        this.addSurfaceBiome(consumer, Climate.Parameter.span(-1.0F, 1.0F), Climate.Parameter.span(-1.0F, 1.0F), Climate.Parameter.span(-1.0F, 1.0F), Climate.Parameter.span(-1.0F, 1.0F), Climate.Parameter.span(-1.0F, 1.0F), 0.0F, COTDBiomes.INFESTED_CAVERNS_KEY);
    }

    @Shadow
    private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> p_187181_, Climate.Parameter p_187182_, Climate.Parameter p_187183_, Climate.Parameter p_187184_, Climate.Parameter p_187185_, Climate.Parameter p_187186_, float p_187187_, ResourceKey<Biome> p_187188_) {
        throw new IllegalStateException("Mixin failed to find shadow of addSurfaceBiome");
    }
}
