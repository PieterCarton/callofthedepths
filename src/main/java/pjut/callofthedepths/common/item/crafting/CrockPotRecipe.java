package pjut.callofthedepths.common.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;
import pjut.callofthedepths.common.block.entity.CrockPotBlockEntity;
import pjut.callofthedepths.common.registry.COTDRecipeTypes;

public class CrockPotRecipe implements Recipe<CrockPotBlockEntity> {
    private final ResourceLocation id;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;

    public CrockPotRecipe(ResourceLocation id, ItemStack result, NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(CrockPotBlockEntity crockPot, Level level) {
        StackedContents stackedcontents = new StackedContents();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < crockPot.getContainerSize(); ++j) {
            ItemStack itemstack = crockPot.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }

        return net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(CrockPotBlockEntity crockPot) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return COTDRecipeTypes.CROCK_POT;
    }

    @Override
    public RecipeSerializer<CrockPotRecipe> getSerializer() {
        return COTDRecipeTypes.CROCK_POT_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CrockPotRecipe> {
        @Override
        public CrockPotRecipe fromJson(ResourceLocation location, JsonObject jsonObject) {

            ItemStack result = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject("result"));
            NonNullList<Ingredient> ingredients = itemsFromJson(jsonObject.getAsJsonArray("ingredients"));

            return new CrockPotRecipe(location, result, ingredients);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray array) {
            NonNullList<Ingredient> list = NonNullList.create();

            for(int i = 0; i < array.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(array.get(i));
                if (!ingredient.isEmpty()) {
                    list.add(ingredient);
                }
            }

            return list;
        }

        @Nullable
        @Override
        public CrockPotRecipe fromNetwork(ResourceLocation location, FriendlyByteBuf buff) {
            ItemStack result = buff.readItem();
            int size = buff.readInt();

            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);

            for (;size > 0; size--) {
                ingredients.set(size, Ingredient.fromNetwork(buff));
            }

            return new CrockPotRecipe(location, result, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buff, CrockPotRecipe crockPotRecipe) {
            buff.writeItem(crockPotRecipe.getResultItem());
            int size = crockPotRecipe.ingredients.size();

            buff.writeInt(size);
            for (Ingredient i: crockPotRecipe.ingredients) {
                i.toNetwork(buff);
            }
        }
    }
}
