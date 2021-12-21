package callofthedepths.common.registry;

import callofthedepths.common.item.COTDFoods;
import callofthedepths.common.setup.CallOfTheDepths;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class COTDItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CallOfTheDepths.MOD_ID);

    public static final RegistryObject<Item> GLOW_BERRY_BAR = ITEMS.register("glow_berry_bar",
            () -> new Item(new Item.Properties().food(COTDFoods.GLOW_BERRY_BAR).tab(CreativeModeTab.TAB_FOOD)));

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
