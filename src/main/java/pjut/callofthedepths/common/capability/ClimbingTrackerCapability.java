package pjut.callofthedepths.common.capability;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

public class ClimbingTrackerCapability {

    public static Capability<ClimbingTracker> CLIMBING_TRACKER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public static final ResourceLocation CLIMBING_TRACKER_LOCATION = new ResourceLocation(CallOfTheDepths.MOD_ID, "climbing_handler");

    public static LazyOptional<ClimbingTracker> get(Entity entity) {
        return entity.getCapability(CLIMBING_TRACKER_CAPABILITY, null);
    }

    /**
     * Non-volatile provider for the climbing tracker capability
     */
    public static class ClimbingTrackerProvider implements ICapabilitySerializable<CompoundTag> {

        private ClimbingTracker instance;
        private Capability<ClimbingTracker> capability;
        private LazyOptional<ClimbingTracker> lazyOptional;

        public ClimbingTrackerProvider(Capability<ClimbingTracker> capability) {
            this.instance = new ClimbingTracker();
            this.capability = capability;

            if (this.instance != null) {
                lazyOptional = LazyOptional.of(() -> instance);
            } else {
                lazyOptional = LazyOptional.empty();
            }
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (instance == null) {
                return LazyOptional.empty();
            }
            return capability.orEmpty(cap, lazyOptional);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();

            nbt.putBoolean("Is Climbing", instance.isClimbing());
            nbt.putInt("Direction", instance.getAttachDirection().ordinal());
            nbt.putInt("Jumps", instance.getJumps());
            nbt.putDouble("Stable Height", instance.getStableHeight());

            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.setClimbing(nbt.getBoolean("Is Climbing"));
            instance.setAttachDirection(Direction.values()[nbt.getInt("Direction")]);
            instance.setJumps(nbt.getInt("Jumps"));
            instance.setStableHeight(nbt.getDouble("Stable Height"));
        }
    }

    @Mod.EventBusSubscriber(modid = CallOfTheDepths.MOD_ID)
    private static class EventHandler {

        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent evt) {
            if (!(evt.getObject() instanceof Player))
                return;
            System.out.println("capability attached");
            evt.addCapability(CLIMBING_TRACKER_LOCATION, new ClimbingTrackerProvider(CLIMBING_TRACKER_CAPABILITY));
        }
    }

    // TODO: separate class for registration of all capabilities
    @Mod.EventBusSubscriber(modid = CallOfTheDepths.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class EventHandler2 {

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent evt) {
            System.out.println("capability registered");
            evt.register(ClimbingTracker.class);
        }
    }
}
