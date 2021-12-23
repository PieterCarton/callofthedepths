package pjut.callofthedepths.common.data;

import pjut.callofthedepths.common.registry.COTDItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class COTDItemTagsProvider extends ItemTagsProvider {
    public COTDItemTagsProvider(DataGenerator p_126530_, BlockTagsProvider p_126531_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126530_, p_126531_, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ItemTags.ARROWS).add(COTDItems.TORCH_ARROW.get());
    }
}
