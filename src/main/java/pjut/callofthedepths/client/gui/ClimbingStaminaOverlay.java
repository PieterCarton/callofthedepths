package pjut.callofthedepths.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import pjut.callofthedepths.common.CallOfTheDepths;
import pjut.callofthedepths.common.capability.ClimbingTracker;
import pjut.callofthedepths.common.capability.ClimbingTrackerCapability;
import pjut.callofthedepths.common.item.ClimbingPickItem;

public class ClimbingStaminaOverlay extends GuiComponent implements IIngameOverlay {
    private static final ResourceLocation CLIMB_GUI = new ResourceLocation(CallOfTheDepths.MOD_ID, "textures/gui/climb_gui.png");
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    private boolean shouldDisplay() {
        Player player = MINECRAFT.player;
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        return mainHand.getItem() instanceof ClimbingPickItem || offHand.getItem() instanceof ClimbingPickItem;
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
        if (!shouldDisplay())
            return;

        // MINECRAFT.getTextureManager().bindForSetup(CLIMB_GUI);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CLIMB_GUI);
        RenderSystem.enableBlend();

        int i = MINECRAFT.getWindow().getGuiScaledWidth() / 2;
        int j = MINECRAFT.getWindow().getGuiScaledHeight() / 2;

        ClimbingTracker climbingTracker = MINECRAFT.player.getCapability(ClimbingTrackerCapability.CLIMBING_TRACKER_CAPABILITY)
                .orElse(new ClimbingTracker());
        // pose, x, y, u0, v0, u1, v1
        for (int k = 0; k < climbingTracker.getJumps(); k++) {
            this.blit(mStack, i + k * 20, j, 0, 0, 16, 16);
        }
        RenderSystem.disableBlend();
        System.out.printf("Rendering %d %d Climbing Thing\n", i, j);
    }
}
