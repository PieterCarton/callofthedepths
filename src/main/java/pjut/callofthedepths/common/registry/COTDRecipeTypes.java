package pjut.callofthedepths.common.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pjut.callofthedepths.common.item.crafting.CrockPotRecipe;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

public class COTDRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CallOfTheDepths.MOD_ID);

    public static final RecipeType<CrockPotRecipe> CROCK_POT = RecipeType.register("callofthedepths:crock_pot");

    public static final RegistryObject<RecipeSerializer<CrockPotRecipe>> CROCK_POT_SERIALIZER = RECIPE_SERIALIZERS.register("crock_pot", () -> new CrockPotRecipe.Serializer());

    public static void init() {
        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
