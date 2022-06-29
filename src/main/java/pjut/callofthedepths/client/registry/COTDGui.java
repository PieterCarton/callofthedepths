package pjut.callofthedepths.client.registry;

import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import pjut.callofthedepths.client.gui.ClimbingStaminaOverlay;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class COTDGui {

    public static final IIngameOverlay CLIMBING_STAMINA_OVERLAY = new ClimbingStaminaOverlay();

    @SubscribeEvent
    public static void onRegisterParticleProviders(FMLClientSetupEvent evt) {
        OverlayRegistry.registerOverlayTop("Climbing Stamina", CLIMBING_STAMINA_OVERLAY);
    }
}
