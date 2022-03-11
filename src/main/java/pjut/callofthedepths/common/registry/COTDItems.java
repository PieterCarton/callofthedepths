package pjut.callofthedepths.common.registry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import pjut.callofthedepths.common.item.COTDFoods;
import pjut.callofthedepths.common.item.ClimbingPickItem;
import pjut.callofthedepths.common.item.DebugItem;
import pjut.callofthedepths.common.item.RopeItem;
import pjut.callofthedepths.common.CallOfTheDepths;
import pjut.callofthedepths.common.item.TorchArrowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class COTDItems {

    public static final CreativeModeTab CALL_OF_THE_DEPTHS = new CreativeModeTab(CallOfTheDepths.MOD_ID + "." + "callofthedepthsitems") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(COTDItems.TORCH_ARROW.get());
        }
    };

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CallOfTheDepths.MOD_ID);

    public static Item.Properties getSharedProperties() {
        return new Item.Properties().tab(CALL_OF_THE_DEPTHS);
    }

    public static final RegistryObject<Item> GLOW_BERRY_BAR = ITEMS.register("glow_berry_bar",
            () -> new Item(getSharedProperties().food(COTDFoods.GLOW_BERRY_BAR)));
    public static final RegistryObject<Item> GRANOLA_BAR = ITEMS.register("granola_bar",
            () -> new Item(getSharedProperties().food(COTDFoods.GRANOLA_BAR)));
    public static final RegistryObject<Item> TORCH_ARROW = ITEMS.register("torch_arrow",
            () -> new TorchArrowItem(getSharedProperties()));
    public static final RegistryObject<Item> ROPE_BLOCK = ITEMS.register("rope_block",
            () -> new RopeItem(COTDBlocks.ROPE_BLOCK.get(), getSharedProperties()));
    public static final RegistryObject<Item> CRAWLER_SPAWN_EGG = ITEMS.register("crawler_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> COTDEntityTypes.CRAWLER.get(), 16499171, 10890612, getSharedProperties()));

    public static final RegistryObject<Item> CLIMBING_PICK = ITEMS.register("climbing_pick",
            () -> new ClimbingPickItem(Tiers.IRON, 4, 2.0f, getSharedProperties(), 5));
    public static final RegistryObject<Item> CLIMBING_PICK_ADVANCED = ITEMS.register("climbing_pick_advanced",
            () -> new ClimbingPickItem(Tiers.DIAMOND, 6, 2.0f, getSharedProperties(), 7));

    public static final RegistryObject<Item> FRACTURED_CARAPACE = ITEMS.register("fractured_carapace",
            () -> new Item(getSharedProperties()));

    public static final RegistryObject<Item> DEBUG_ITEM = ITEMS.register("debug_item",
            () -> new DebugItem(getSharedProperties()));


    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
