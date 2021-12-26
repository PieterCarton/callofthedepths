package pjut.callofthedepths.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import pjut.callofthedepths.common.data.loot.COTDLootTableProvider;

@Mod.EventBusSubscriber(modid = "callofthedepths", bus = Mod.EventBusSubscriber.Bus.MOD)
public class COTDDataGeneration {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        ExistingFileHelper helper = evt.getExistingFileHelper();

        if (evt.includeServer()) {
            BlockTagsProvider provider = new COTDBlockTagsProvider(generator, "callofthedepths", helper);
            generator.addProvider(provider);
            generator.addProvider(new COTDItemTagsProvider(generator, provider, "callofthedepths", helper));
            generator.addProvider(new COTDRecipeProvider(generator));
            generator.addProvider(new COTDLootTableProvider(generator));
        }
        if (evt.includeClient()) {
            generator.addProvider(new COTDItemModelProvider(generator, helper));
        }
    }
}
