package pjut.callofthedepths.common.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.tags.Tag;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import pjut.callofthedepths.common.registry.COTDRecipeTypes;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class CrockPotRecipeBuilder implements RecipeBuilder {
    private Item result;
    private int count;

    private String group;

    private int cookTime;
    private List<Ingredient> ingredients = new LinkedList<>();

    public static CrockPotRecipeBuilder crockPotRecipe(Item result, int count) {
        return new CrockPotRecipeBuilder(result, count);
    }

    public CrockPotRecipeBuilder(Item result, int count) {
        this.result = result;
        this.count = count;
    }

    public CrockPotRecipeBuilder requires(Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    public CrockPotRecipeBuilder requires(Tag<Item> tag, int count) {
        requires(Ingredient.of(tag), count);
        return this;
    }

    public CrockPotRecipeBuilder requires(Item item, int count) {
        requires(Ingredient.of(item), count);
        return this;
    }

    public CrockPotRecipeBuilder withCookTime(int cookTime) {
        this.cookTime = cookTime;
        return this;
    }

    @Override
    public CrockPotRecipeBuilder unlockedBy(String criterion, CriterionTriggerInstance trigger) {
        return this;
    }

    @Override
    public CrockPotRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation location) {
        consumer.accept(new Result(this, location));
    }

    public static class Result implements FinishedRecipe {

        private CrockPotRecipeBuilder builder;
        private ResourceLocation location;

        public Result(CrockPotRecipeBuilder builder, ResourceLocation location) {
            this.builder = builder;
            this.location = location;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            jsonObject.addProperty("group", builder.group);
            jsonObject.addProperty("cookTime", builder.cookTime);

            JsonArray ingredients = new JsonArray();
            for (Ingredient ingredient: builder.ingredients) {
                ingredients.add(ingredient.toJson());
            }

            jsonObject.add("ingredients", ingredients);
            JsonObject result = new JsonObject();
            result.addProperty("item", Registry.ITEM.getKey(this.builder.result).toString());
            if (this.builder.count > 1) {
                result.addProperty("count", this.builder.count);
            }

            jsonObject.add("result", result);
        }

        @Override
        public ResourceLocation getId() {
            return location;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return COTDRecipeTypes.CROCK_POT_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return new ResourceLocation("");
        }
    }
}
