package pjut.callofthedepths.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import pjut.callofthedepths.common.registry.COTDItems;

import java.util.function.Consumer;

public class COTDRecipeProvider extends RecipeProvider {
    public COTDRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(COTDItems.TORCH_ARROW.get(),4)
                .define('X', ItemTags.COALS)
                .define('#', Tags.Items.RODS_WOODEN)
                .define('Y', Tags.Items.FEATHERS)
                .pattern("X")
                .pattern("#")
                .pattern("Y")
                .unlockedBy("has_components", has(ItemTags.COALS))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(COTDItems.ROPE_BLOCK.get(),8)
                .define('S', Tags.Items.STRING)
                .pattern("  S")
                .pattern(" S ")
                .pattern("S  ")
                .unlockedBy("has_components", has(Tags.Items.STRING))
                .save(recipeConsumer);
    }
}
