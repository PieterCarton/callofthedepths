package pjut.callofthedepths.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import pjut.callofthedepths.common.registry.COTDBlocks;
import pjut.callofthedepths.common.CallOfTheDepths;

public class COTDBlockStateProvider extends BlockStateProvider {
    public COTDBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, CallOfTheDepths.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(COTDBlocks.GYPSUM.get(), getExistingFile("gypsum"));
        stairsBlock((StairBlock) COTDBlocks.GYPSUM_STAIRS.get(), getExistingFile("gypsum_stairs"), getExistingFile("gypsum_stairs_inner"), getExistingFile("gypsum_stairs_outer"));
        slabBlock((SlabBlock) COTDBlocks.GYPSUM_SLAB.get(), getExistingFile("gypsum_slab"), getExistingFile("gypsum_slab_top"), getExistingFile("gypsum"));

        simpleBlock(COTDBlocks.POLISHED_GYPSUM.get(), getExistingFile("polished_gypsum"));
        stairsBlock((StairBlock) COTDBlocks.POLISHED_GYPSUM_STAIRS.get(), getExistingFile("polished_gypsum_stairs"), getExistingFile("polished_gypsum_stairs_inner"), getExistingFile("polished_gypsum_stairs_outer"));
        slabBlock((SlabBlock) COTDBlocks.POLISHED_GYPSUM_SLAB.get(), getExistingFile("polished_gypsum_slab"), getExistingFile("polished_gypsum_slab_top"), getExistingFile("polished_gypsum"));

        simpleBlock(COTDBlocks.SILKEN_HUSK.get(), getExistingFile("silken_husk"));
        simpleBlock(COTDBlocks.CROCK_POT.get(), getExistingFile("crock_pot"));
    }

    private ModelFile getExistingFile(String name) {
        return new ModelFile.ExistingModelFile(new ResourceLocation("callofthedepths:block/" + name), models().existingFileHelper);
    }
}
