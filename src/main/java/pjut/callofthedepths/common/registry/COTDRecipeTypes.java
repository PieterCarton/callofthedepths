package pjut.callofthedepths.common.registry;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import pjut.callofthedepths.common.CallOfTheDepths;
import pjut.callofthedepths.common.item.crafting.CrockPotRecipe;

/**
 * Recipe type initialization code shamelessly stolen from Immersive Engineering:
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/1.18.2/src/main/java/blusunrize/immersiveengineering/common/register/IERecipes.java
 */
@EventBusSubscriber(modid = CallOfTheDepths.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class COTDRecipeTypes {
    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> evt) {
        CrockPotRecipe.TYPE = register("crock_pot");
    }

    public static <T extends Recipe<?>> RecipeType<T> register(String name){
        return RecipeType.register(CallOfTheDepths.MOD_ID + name);
    }
}
