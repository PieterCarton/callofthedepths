package pjut.callofthedepths.common.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.Tags;

public class TestFeature extends Feature<NoneFeatureConfiguration> {
    public TestFeature(Codec<NoneFeatureConfiguration> p_65786_) {
        super(p_65786_);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> placeContext) {
        BlockState pole = Blocks.OAK_FENCE.defaultBlockState();

        WorldGenLevel level = placeContext.level();
        BlockPos origin = placeContext.origin();

        level.setBlock(origin, pole,2);
        level.setBlock(origin.above(), pole,2);

        return true;
    }
}
