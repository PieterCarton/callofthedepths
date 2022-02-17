package pjut.callofthedepths.client.debug;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.PathfindingRenderer;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CallOfTheDepths.MOD_ID)
public class DebugEventHandler {

    public static Map<Integer, Path> pathMap = new HashMap<>();

    public static synchronized void addPath(int id, Path path) {
        synchronized (DebugEventHandler.class) {
            pathMap.put(id, path);
        }

    }

    @SubscribeEvent
    public static void drawDebug(RenderLevelLastEvent evt) {
        Minecraft minecraft = Minecraft.getInstance();
        Vec3 camVec = minecraft.gameRenderer.getMainCamera().getPosition();

        PathfindingRenderer pathfindingRenderer = Minecraft.getInstance().debugRenderer.pathfindingRenderer;
        synchronized (DebugEventHandler.class) {
            for (Integer id: pathMap.keySet()) {
                pathfindingRenderer.addPath(id, pathMap.get(id), 64.0f);
            }
            pathMap.clear();
        }
        PoseStack evtPostStack = evt.getPoseStack();
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.mulPoseMatrix(evtPostStack.last().pose());
        RenderSystem.applyModelViewMatrix();
        pathfindingRenderer.render(evtPostStack, minecraft.renderBuffers().bufferSource(), camVec.x(), camVec.y(), camVec.z());
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
