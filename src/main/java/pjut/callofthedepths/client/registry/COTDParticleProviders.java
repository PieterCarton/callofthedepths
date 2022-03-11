package pjut.callofthedepths.client.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import pjut.callofthedepths.client.CrockPotBubbleParticle;
import pjut.callofthedepths.common.registry.COTDParticleTypes;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class COTDParticleProviders {
    @SubscribeEvent
    public static void onRegisterParticleProviders(ParticleFactoryRegisterEvent evt) {
        ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;

        particleEngine.register(COTDParticleTypes.CROCK_POT_BUBBLE.get(), CrockPotBubbleParticle.Provider::new);
    }
}
