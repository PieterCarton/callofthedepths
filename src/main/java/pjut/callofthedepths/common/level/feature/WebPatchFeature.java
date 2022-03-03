package pjut.callofthedepths.common.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import pjut.callofthedepths.common.block.WebCarpetBlock;
import pjut.callofthedepths.common.registry.COTDBlocks;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class WebPatchFeature extends Feature<NoneFeatureConfiguration> {
    public WebPatchFeature(Codec<NoneFeatureConfiguration> p_65786_) {
        super(p_65786_);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos blockPos = context.origin();
        Random random = context.random();

        int radius = random.nextInt(6);
        int scatterRadius = radius + random.nextInt(1 + radius / 2);

        MultifaceBlock webCarpetBlock = (MultifaceBlock) COTDBlocks.WEB_CARPET.get();

        for (int x = -scatterRadius; x <= scatterRadius; x++) {
            for (int y = -scatterRadius; y <= scatterRadius; y++) {
                for (int z = -scatterRadius; z <= scatterRadius; z++) {
                    BlockPos currentPos = blockPos.offset(x, y, z);
                    int distanceSquared = x * x + y * y + z * z;
                    if (isAir(level, currentPos) && distanceSquared < radius * radius) {
                        tryPlaceWebCarpet(level, currentPos, webCarpetBlock);
                    } else if (isAir(level, currentPos) && distanceSquared < scatterRadius * scatterRadius) {
                        if ((random.nextInt() & 1) == 1) {
                            tryPlaceWebCarpet(level, currentPos, webCarpetBlock);
                        }
                    }
                }
            }
        }

        return true;
    }

    private static void tryPlaceWebCarpet(WorldGenLevel level, BlockPos pos, MultifaceBlock webCarpetBlock) {
        BlockState current = level.getBlockState(pos);
        BlockState next = nextBlockState(level, current, pos, webCarpetBlock);

        while (next != null) {
            current = next;
            next = nextBlockState(level, current, pos, webCarpetBlock);
        }

        level.setBlock(pos, current, 3);
    }

    private static BlockState nextBlockState(WorldGenLevel level, BlockState blockstate, BlockPos pos, MultifaceBlock webCarpetBlock) {
        return Arrays.stream(Direction.values()).map((direction) -> {
            return webCarpetBlock.getStateForPlacement(blockstate, level, pos, direction);
        }).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
