package pjut.callofthedepths.common.registry;

import pjut.callofthedepths.common.item.COTDFoods;
import pjut.callofthedepths.common.item.RopeItem;
import pjut.callofthedepths.common.setup.CallOfTheDepths;
import pjut.callofthedepths.common.item.TorchArrowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class COTDItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CallOfTheDepths.MOD_ID);

    public static final RegistryObject<Item> GLOW_BERRY_BAR = ITEMS.register("glow_berry_bar",
            () -> new Item(new Item.Properties().food(COTDFoods.GLOW_BERRY_BAR).tab(CreativeModeTab.TAB_FOOD)));
    public static final RegistryObject<Item> TORCH_ARROW = ITEMS.register("torch_arrow",
            () -> new TorchArrowItem(new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));
    public static final RegistryObject<Item> ROPE_BLOCK = ITEMS.register("rope_block",
            () -> new RopeItem(COTDBlocks.ROPE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
