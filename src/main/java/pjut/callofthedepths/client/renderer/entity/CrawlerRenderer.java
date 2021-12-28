package pjut.callofthedepths.client.renderer.entity;

import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import pjut.callofthedepths.client.model.entity.CrawlerModel;
import pjut.callofthedepths.common.entity.Crawler;

public class CrawlerRenderer extends MobRenderer<Crawler, CrawlerModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("callofthedepths:textures/entity/test.png");

    public CrawlerRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new CrawlerModel(), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(Crawler p_114482_) {
        return TEXTURE;
    }
}
