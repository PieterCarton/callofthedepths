package pjut.callofthedepths.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

public class COTDItemModelProvider extends ItemModelProvider {
    public COTDItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CallOfTheDepths.MOD_ID, existingFileHelper);
    }

    @Override
    public void registerModels() {
        getBuilder("torch_arrow").parent(getExistingFile(new ResourceLocation("minecraft:item/generated")))
                .texture("layer0", "item/torch_arrow");
    }
}
