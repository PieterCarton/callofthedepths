package pjut.callofthedepths.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import pjut.callofthedepths.common.registry.COTDBlocks;

public class COTDBlockTagsProvider extends BlockTagsProvider {
    public COTDBlockTagsProvider(DataGenerator p_126511_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126511_, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.NEEDS_STONE_TOOL).add(COTDBlocks.GYPSUM.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(COTDBlocks.GYPSUM.get());
    }
}
