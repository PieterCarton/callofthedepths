package pjut.callofthedepths.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import pjut.callofthedepths.common.block.entity.CrockPotBlockEntity;

public class CrockPotRenderer implements BlockEntityRenderer<CrockPotBlockEntity> {

    private final BlockRenderDispatcher blockRenderDispatcher;

    public CrockPotRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(CrockPotBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.0D, 0.5D, 0.0D);

        TextureAtlasSprite texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("block/dirt"));
        texture.getU0();
        BakedQuadBuilder builder = new BakedQuadBuilder(texture);
        builder.setQuadOrientation(Direction.DOWN);
        builder.setQuadTint(0x0000FFA0);

        putVertex(builder,  0.0f, 0.0f, 0.0f, texture.getU0(), texture.getV0());
        putVertex(builder,  0.0f, 0.0f, 1.0f, texture.getU0(), texture.getV1());
        putVertex(builder,  1.0f, 0.0f, 1.0f, texture.getU1(), texture.getV1());
        putVertex(builder,  1.0f, 0.0f, 0.0f, texture.getU1(), texture.getV0());


        BakedQuad quad = builder.build();
        PoseStack.Pose pose = poseStack.last();



        VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());
        buffer.putBulkData(pose, quad, 1.0f, 1.0f, 1.0f, 0, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    private void putVertex(BakedQuadBuilder builder, float x, float y, float z, float u, float v) {
        builder.put(0, x, y, z, 0.0f);              // position
        builder.put(1, 0.0f, 0.0f, 1.0f, 1.0f);     // color
        builder.put(2, u, v, 1.0f, 0.0f);           // uv0
        builder.put(3, u, v, 1.0f, 0.0f);           // uv2
        builder.put(4, 0.0f, 1.0f, 0.0f, 0.0f);     // normal
        builder.put(5);                                   // padding
    }

    @Override
    public boolean shouldRenderOffScreen(CrockPotBlockEntity p_112306_) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(p_112306_);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(CrockPotBlockEntity p_173568_, Vec3 p_173569_) {
        return BlockEntityRenderer.super.shouldRender(p_173568_, p_173569_);
    }
}
