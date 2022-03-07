package pjut.callofthedepths.common.registry;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import pjut.callofthedepths.client.renderer.blockentity.CrockPotRenderer;

public class COTDBlockEntityRenderers {
    public static void register() {
        assert COTDBlockEntities.CROCK_POT_BE != null: "Block entities were not initialized before registration of their renderers";

        BlockEntityRenderers.register(COTDBlockEntities.CROCK_POT_BE, CrockPotRenderer::new);
    }
}
