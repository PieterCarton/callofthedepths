package pjut.callofthedepths.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

public class COTDBlockModelProvider extends BlockModelProvider {
    public COTDBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CallOfTheDepths.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        cubeAll("gypsum", new ResourceLocation("callofthedepths:block/gypsum"));
    }
}
