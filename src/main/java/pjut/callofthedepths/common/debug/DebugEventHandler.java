package pjut.callofthedepths.common.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DebugEventHandler {
    @SubscribeEvent
    public static void onDebugMessage(ClientChatEvent evt) {
        if (evt.getMessage().equals("!debug")) {
            System.out.println("possible biomes");
//            Biomes
//            for (Minecraft.getInstance().player.level.getC) {
//                System.out.println(key.toString());
//            }
        }
    }
}
