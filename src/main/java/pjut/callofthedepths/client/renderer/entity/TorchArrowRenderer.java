package pjut.callofthedepths.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import pjut.callofthedepths.common.entity.projectile.TorchArrow;

public class TorchArrowRenderer extends ArrowRenderer<TorchArrow> {

    public static final ResourceLocation ARROW_LOCATION = new ResourceLocation("callofthedepths", "textures/entity/projectiles/torch_arrow.png");

    public TorchArrowRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(TorchArrow p_114482_) {
        return ARROW_LOCATION;
    }
}
