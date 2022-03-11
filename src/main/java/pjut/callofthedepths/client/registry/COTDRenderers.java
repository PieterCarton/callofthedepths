package pjut.callofthedepths.client.registry;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import pjut.callofthedepths.client.renderer.blockentity.CrockPotRenderer;
import pjut.callofthedepths.client.renderer.entity.CrawlerRenderer;
import pjut.callofthedepths.client.renderer.entity.TorchArrowRenderer;
import pjut.callofthedepths.common.registry.COTDBlockEntities;
import pjut.callofthedepths.common.registry.COTDBlocks;
import pjut.callofthedepths.common.registry.COTDEntityTypes;

public class COTDRenderers {
    public static void register() {
        assert COTDBlockEntities.CROCK_POT_BE != null: "Block entities were not initialized before registration of their renderers";

        BlockEntityRenderers.register(COTDBlockEntities.CROCK_POT_BE, CrockPotRenderer::new);

        EntityRenderers.register(COTDEntityTypes.TORCH_ARROW.get(), TorchArrowRenderer::new);
        EntityRenderers.register(COTDEntityTypes.CRAWLER.get(), CrawlerRenderer::new);

        ItemBlockRenderTypes.setRenderLayer(COTDBlocks.ROPE_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(COTDBlocks.CROCK_POT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(COTDBlocks.WEB_CARPET.get(), RenderType.tripwire());
    }
}
