package pjut.callofthedepths.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import pjut.callofthedepths.common.block.entity.CrockPotBlockEntity;

public class CrockPotRenderer implements BlockEntityRenderer<CrockPotBlockEntity> {

    protected static final int WATER_COLOR = 0x3f76e4;
    protected static final float WATER_COLOR_R = 0x3f / 255f;
    protected static final float WATER_COLOR_G = 0x76 / 255f;
    protected static final float WATER_COLOR_B = 0xe4 / 255f;

    private static final Vec3 FLUID_BOTTOM_LEFT = new Vec3(3.0f / 16.0f, 0.0f, 3.0f / 16.0f);
    private static final Vec3 FLUID_TOP_RIGHT = new Vec3(13.0f / 16.0f, 0.0f, 13.0f / 16.0f);
    private final ItemRenderer itemRenderer;

    public CrockPotRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(CrockPotBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();

        renderItems(entity, partialTicks, poseStack, bufferSource, combinedLight, combinedOverlay);

        poseStack.translate(0.0D, 4.5D / 16.0D, 0.0D);

        TextureAtlasSprite texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("block/water_still"));
        texture.getU0();
        BakedQuadBuilder builder = new BakedQuadBuilder(texture);
        builder.setQuadOrientation(Direction.DOWN);
        builder.setQuadTint(0);

        putVertex(builder, (float)FLUID_BOTTOM_LEFT.x(), 0.0f, (float)FLUID_BOTTOM_LEFT.z(), interpolate(3.0f / 16.0f, texture.getU0(), texture.getU1()), interpolate(3.0f / 16.0f, texture.getV0(), texture.getV1()));
        putVertex(builder, (float)FLUID_BOTTOM_LEFT.x(), 0.0f, (float)FLUID_TOP_RIGHT.z(), interpolate(3.0f / 16.0f, texture.getU0(), texture.getU1()), interpolate(13.0f / 16.0f, texture.getV0(), texture.getV1()));
        putVertex(builder, (float)FLUID_TOP_RIGHT.x(), 0.0f, (float)FLUID_TOP_RIGHT.z(), interpolate(13.0f / 16.0f, texture.getU0(), texture.getU1()), interpolate(13.0f / 16.0f, texture.getV0(), texture.getV1()));
        putVertex(builder, (float)FLUID_TOP_RIGHT.x(), 0.0f, (float)FLUID_BOTTOM_LEFT.z(), interpolate(13.0f / 16.0f, texture.getU0(), texture.getU1()), interpolate(3.0f / 16.0f, texture.getV0(), texture.getV1()));

        BakedQuad quad = builder.build();
        PoseStack.Pose pose = poseStack.last();

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());
        buffer.putBulkData(pose, quad, WATER_COLOR_R, WATER_COLOR_G, WATER_COLOR_B, combinedLight, combinedOverlay/*OverlayTexture.NO_OVERLAY*/);

        poseStack.popPose();
    }

    private void putVertex(BakedQuadBuilder builder, float x, float y, float z, float u, float v) {
        builder.put(0, x, y, z, 0.0f);              // position
        builder.put(1, 1.0f, 0.0f, 1.0f, 0.5f);     // color
        builder.put(2, u, v, 1.0f, 0.0f);           // uv0
        builder.put(3, u, v, 1.0f, 0.0f);           // uv2
        builder.put(4, 0.0f, 1.0f, 0.0f, 0.0f);     // normal
        builder.put(5);                                   // padding
    }

    private float interpolate(float alpha,float min, float max) {
        return min + (max - min) * alpha;
    }

    private void renderItems(CrockPotBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.2, 0.5);

        for (int slot = 0; slot < entity.getContainerSize(); slot++) {
            ItemStack stack = entity.getItem(slot);
            poseStack.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0f, 360f / 8f, 0f)));
            if (!stack.isEmpty()) {
                poseStack.pushPose();

                poseStack.translate(0.2, 0, 0);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.mulPose(Quaternion.fromXYZDegrees(new Vector3f(90f, 0f, 0f)));


                BakedModel itemModel = this.itemRenderer.getModel(stack, entity.getLevel(), null, 0);
                this.itemRenderer.render(stack, ItemTransforms.TransformType.GROUND, false, poseStack, bufferSource, combinedLight, combinedOverlay, itemModel);

                poseStack.popPose();
            }
        }

        poseStack.popPose();
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
