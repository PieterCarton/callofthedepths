package pjut.callofthedepths.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
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
import pjut.callofthedepths.common.util.ClimbingUtil;

public class ClimbingStaminaOverlay extends GuiComponent implements IIngameOverlay {
    private static final ResourceLocation CLIMB_GUI = new ResourceLocation(CallOfTheDepths.MOD_ID, "textures/gui/climb_gui.png");
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    private boolean shouldDisplay() {
        ClimbingTracker climbingTracker = MINECRAFT.player.getCapability(ClimbingTrackerCapability.CLIMBING_TRACKER_CAPABILITY)
                .orElse(new ClimbingTracker());

        return climbingTracker.isClimbing() || climbingTracker.getJumps() > 0;
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
        if (!shouldDisplay())
            return;

        LocalPlayer player = MINECRAFT.player;
        ClimbingTracker climbingTracker = ClimbingTrackerCapability.get(player)
                .orElse(new ClimbingTracker());

        int jumps = climbingTracker.getJumps();
        int maxJumps = ClimbingUtil.getMaxJumps(player);

        // find centre of screen
        Window window = MINECRAFT.getWindow();
        int i = window.getGuiScaledWidth() / 2;
        int j = window.getGuiScaledHeight() / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CLIMB_GUI);
        RenderSystem.enableBlend();

        // draw max stamina
        int iconDistance = 12;
        for (int k = 0; k < maxJumps; k++) {
            // pose, x, y, u0, v0, u1, v1
            this.blit(mStack, i + k * iconDistance, j, 0, 0, 8, 8);
        }

        // draw current stamina
        for (int k = 0; k < maxJumps - jumps; k++) {
            // pose, x, y, u0, v0, u1, v1
            this.blit(mStack, i + k * iconDistance, j, 9, 0, 8, 8);
        }

        RenderSystem.disableBlend();
    }
}
