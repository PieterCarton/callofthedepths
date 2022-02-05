package pjut.callofthedepths.common.setup;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.event.lifecycle.*;
import pjut.callofthedepths.client.renderer.entity.CrawlerRenderer;
import pjut.callofthedepths.client.renderer.entity.TorchArrowRenderer;
import pjut.callofthedepths.common.entity.projectile.TorchArrow;
import pjut.callofthedepths.common.network.COTDPacketHandler;
import pjut.callofthedepths.common.registry.COTDBlocks;
import pjut.callofthedepths.common.registry.COTDEntityTypes;
import pjut.callofthedepths.common.registry.COTDFeaturePlacement;
import pjut.callofthedepths.common.registry.COTDFeatures;
import pjut.callofthedepths.common.registry.COTDItems;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod("callofthedepths")
public class CallOfTheDepths {
    public static final String MOD_ID = "callofthedepths";
    private static final Logger LOGGER = LogManager.getLogger();

    public CallOfTheDepths() {
        COTDItems.init();
        COTDBlocks.init();
        COTDEntityTypes.init();
        COTDFeatures.init();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(COTDFeaturePlacement::register);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onPostSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelBakeEvent);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        System.out.println("CommonSetup");
        COTDPacketHandler.registerPackets();
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public void onClientSetup(FMLClientSetupEvent evt) {
        LOGGER.info("HELLO from client setup");
        EntityRenderers.register(COTDEntityTypes.TORCH_ARROW.get(), TorchArrowRenderer::new);
        EntityRenderers.register(COTDEntityTypes.CRAWLER.get(), CrawlerRenderer::new);

        ItemBlockRenderTypes.setRenderLayer(COTDBlocks.ROPE_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(COTDBlocks.WEB_CARPET.get(), RenderType.tripwire());
    }

    public void onPostSetup(FMLLoadCompleteEvent evt) {
        DispenserBlock.registerBehavior(COTDItems.TORCH_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level p_123407_, Position p_123408_, ItemStack p_123409_) {
                AbstractArrow arrow = new TorchArrow(p_123407_, p_123408_.x(), p_123408_.y(), p_123408_.z());
                arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return arrow;
            }
        });
    }

    public void onModelBakeEvent(ModelBakeEvent evt) {

    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
