package pjut.callofthedepths.common;

import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pjut.callofthedepths.client.registry.COTDRenderers;
import pjut.callofthedepths.common.entity.projectile.TorchArrow;
import pjut.callofthedepths.common.network.COTDPacketHandler;
import pjut.callofthedepths.common.registry.COTDBiomes;
import pjut.callofthedepths.common.registry.COTDBlocks;
import pjut.callofthedepths.common.registry.COTDEntityTypes;
import pjut.callofthedepths.common.registry.COTDFeaturePlacement;
import pjut.callofthedepths.common.registry.COTDFeatures;
import pjut.callofthedepths.common.registry.COTDItems;
import pjut.callofthedepths.common.registry.COTDParticleTypes;
import pjut.callofthedepths.common.registry.COTDRecipeTypes;

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
        COTDBiomes.init();
        COTDRecipeTypes.init();
        COTDParticleTypes.init();

        // Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::setup);
        modEventBus.addListener(COTDFeaturePlacement::register);
        // Register the enqueueIMC method for modloading
        modEventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modEventBus.addListener(this::processIMC);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onPostSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        COTDPacketHandler.registerPackets();
        COTDBiomes.registerBiomes();
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
        COTDRenderers.register();
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
}
