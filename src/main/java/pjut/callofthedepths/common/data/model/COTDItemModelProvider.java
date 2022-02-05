package pjut.callofthedepths.common.data.model;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
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
        getBuilder("granola_bar").parent(getExistingFile(new ResourceLocation("minecraft:item/generated")))
                .texture("layer0", "item/granola_bar");
        getBuilder("glow_berry_bar").parent(getExistingFile(new ResourceLocation("minecraft:item/generated")))
                .texture("layer0", "item/glow_berry_bar");
        getBuilder("crawler_spawn_egg").parent(getExistingFile(new ResourceLocation("minecraft:item/generated")))
                .texture("layer0", "minecraft:item/spawn_egg")
                .texture("layer1", "minecraft:item/spawn_egg");
        getBuilder("crawler_spawn_egg").parent(getExistingFile(new ResourceLocation("minecraft:item/generated")))
                .texture("layer0", "minecraft:item/spawn_egg")
                .texture("layer1", "minecraft:item/spawn_egg");
        getBuilder("web_carpet").parent(getExistingFile(new ResourceLocation("minecraft:item/generated")))
                .texture("layer0", "block/web_carpet");
        getBuilder("climbing_pick").parent(getExistingFile(new ResourceLocation("minecraft:item/handheld")))
                .texture("layer0", "item/climbing_pick");

        getBuilder("gypsum").parent(getExistingFile(new ResourceLocation("callofthedepths:block/gypsum")));
        getBuilder("silken_husk").parent(getExistingFile(new ResourceLocation("callofthedepths:block/silken_husk")));
    }
}
