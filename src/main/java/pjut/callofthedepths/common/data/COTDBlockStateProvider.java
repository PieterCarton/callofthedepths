package pjut.callofthedepths.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import pjut.callofthedepths.common.registry.COTDBlocks;
import pjut.callofthedepths.common.registry.COTDItems;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

public class COTDBlockStateProvider extends BlockStateProvider {
    public COTDBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, CallOfTheDepths.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(COTDBlocks.GYPSUM.get(), getExistingFile("gypsum"));
    }

    private ModelFile getExistingFile(String name) {
        return new ModelFile.ExistingModelFile(new ResourceLocation("callofthedepths:block/" + name), models().existingFileHelper);
    }
}
