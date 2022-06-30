package pjut.callofthedepths.common.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;
import pjut.callofthedepths.common.block.entity.CrockPotBlockEntity;
import pjut.callofthedepths.common.registry.COTDRecipeSerializers;

public class CrockPotRecipe implements Recipe<CrockPotBlockEntity> {
    public static RecipeType<CrockPotRecipe> TYPE;
    public static final int INGREDIENT_COUNT = 8;

    private final ResourceLocation id;
    private final int cookTime;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    public CrockPotRecipe(ResourceLocation id, ItemStack result, NonNullList<Ingredient> ingredients, int cookTime) {
        this.id = id;
        this.result = result;
        this.ingredients = ingredients;
        this.cookTime = cookTime;
    }

    @Override
    public boolean matches(CrockPotBlockEntity crockPot, Level level) {
        StackedContents stackedcontents = new StackedContents();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < INGREDIENT_COUNT; ++j) {
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
        for (int slot = 0; slot < INGREDIENT_COUNT; slot++) {
            crockPot.setItem(slot, ItemStack.EMPTY);
        }
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result.copy();
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    @Override
    public RecipeSerializer<CrockPotRecipe> getSerializer() {
        return COTDRecipeSerializers.CROCK_POT_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CrockPotRecipe> {
        @Override
        public CrockPotRecipe fromJson(ResourceLocation location, JsonObject jsonObject) {

            ItemStack result = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject("result"));
            NonNullList<Ingredient> ingredients = itemsFromJson(jsonObject.getAsJsonArray("ingredients"));
            int cookTime = jsonObject.get("cookTime").getAsInt();


            return new CrockPotRecipe(location, result, ingredients, cookTime);
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

            int cookTime = buff.readInt();

            return new CrockPotRecipe(location, result, ingredients, cookTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buff, CrockPotRecipe crockPotRecipe) {
            buff.writeItem(crockPotRecipe.getResultItem());
            int size = crockPotRecipe.ingredients.size();

            buff.writeInt(size);
            for (Ingredient i: crockPotRecipe.ingredients) {
                i.toNetwork(buff);
            }

            buff.writeInt(crockPotRecipe.getCookTime());
        }
    }
}
