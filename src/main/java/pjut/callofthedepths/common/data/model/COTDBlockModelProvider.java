package pjut.callofthedepths.common.data.model;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import pjut.callofthedepths.common.CallOfTheDepths;

public class COTDBlockModelProvider extends BlockModelProvider {
    public COTDBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CallOfTheDepths.MOD_ID, existingFileHelper);
    }

    private static final ResourceLocation GYPSUM_LOCATION = new ResourceLocation("callofthedepths:block/gypsum");
    private static final ResourceLocation POLISHED_GYPSUM_LOCATION = new ResourceLocation("callofthedepths:block/polished_gypsum");

    @Override
    protected void registerModels() {

        cubeAll("gypsum", GYPSUM_LOCATION);

        stairs("gypsum_stairs", GYPSUM_LOCATION, GYPSUM_LOCATION, GYPSUM_LOCATION);
        stairsInner("gypsum_stairs_inner", GYPSUM_LOCATION, GYPSUM_LOCATION, GYPSUM_LOCATION);
        stairsOuter("gypsum_stairs_outer", GYPSUM_LOCATION, GYPSUM_LOCATION, GYPSUM_LOCATION);

        slab("gypsum_slab", GYPSUM_LOCATION, GYPSUM_LOCATION, GYPSUM_LOCATION);
        slabTop("gypsum_slab_top", GYPSUM_LOCATION, GYPSUM_LOCATION, GYPSUM_LOCATION);

        cubeAll("polished_gypsum", POLISHED_GYPSUM_LOCATION);

        stairs("polished_gypsum_stairs", POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION);
        stairsInner("polished_gypsum_stairs_inner", POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION);
        stairsOuter("polished_gypsum_stairs_outer", POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION);

        slab("polished_gypsum_slab", POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION);
        slabTop("polished_gypsum_slab_top", POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION, POLISHED_GYPSUM_LOCATION);

        cubeAll("silken_husk", new ResourceLocation("callofthedepths:block/silken_husk"));
    }
}
