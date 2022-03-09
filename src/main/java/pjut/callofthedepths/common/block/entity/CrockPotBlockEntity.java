package pjut.callofthedepths.common.block.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;
import pjut.callofthedepths.common.registry.COTDBlockEntities;

import java.util.Collections;
import java.util.Set;

public class CrockPotBlockEntity extends BlockEntity implements Container {
    public static final Set<ResourceLocation> acceptedFoodTags = fillAcceptedFoodTags();

    public CrockPotBlockEntity(BlockPos pos, BlockState state) {
        super(COTDBlockEntities.CROCK_POT_BE, pos, state);
    }

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
    // private LazyOptional<IItemHandler> itemHandler; /* TODO: add capability for exposing inventory */
    private boolean full = false;
    private boolean heated = false;

    // only execute on client
    public InteractionResult interact(ItemStack interactionItem, Player player) {

        System.out.println("interaction propagated to block entity");
        System.out.println("clicked on crock pot with: " + interactionItem.toString());

        LazyOptional<IFluidHandlerItem> fluidHandler = interactionItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        if (fluidHandler.isPresent()) {
            System.out.println("item is a fluid handler containing");
            fluidHandler.ifPresent(h -> {
                System.out.println(h.getFluidInTank(0).getFluid().toString());
                System.out.println("water, lava, half bucket of water");

                FluidStack desiredWater = new FluidStack(Fluids.WATER, 1000);
                FluidStack tryDrainResult = h.drain(desiredWater, IFluidHandler.FluidAction.SIMULATE);

                if (tryDrainResult.getFluid().equals(Fluids.WATER) && tryDrainResult.getAmount() == 1000) {
                    System.out.println("filled");
                    h.drain(desiredWater, IFluidHandler.FluidAction.EXECUTE);
                    this.full = true;
                }
            });
            return InteractionResult.CONSUME;
        } else if (interactionItem.getItem().equals(Items.BOWL)) {
            interactionItem.split(1);
            player.addItem(new ItemStack(Items.MUSHROOM_STEW));

            return InteractionResult.CONSUME;
        } else if (interactionItem.getItem().equals(Items.BROWN_MUSHROOM)) {
            System.out.println("adding item to inventory");
            System.out.println("tags " + interactionItem.getItem().getTags());
            for (int slot = 0; slot < inventory.size(); slot++) {
                if (this.getItem(slot).equals(ItemStack.EMPTY)) {
                    this.setItem(slot, interactionItem.split(1));
                    this.setChanged();
                    break;
                }
            }
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
            this.setChanged();
            return InteractionResult.CONSUME;
        }

        return InteractionResult.FAIL;
    }

    public boolean isFull() {
        return full;
    }

    public boolean isHeated() {
        return heated;
    }

    public void setHeated(boolean heated) {
        this.heated = heated;
    }

    public static void checkHeated(BlockGetter level, BlockPos pos, CrockPotBlockEntity crockPot) {
        System.out.println("should be heated? " + level.getBlockState(pos.below()).is(Blocks.CAMPFIRE));
        System.out.println("current blockstate: " + level.getBlockState(pos.below()));
        crockPot.setHeated(level.getBlockState(pos.below()).is(Blocks.CAMPFIRE));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        System.out.println("got update tag");
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        System.out.println("handled update tag");
        load(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.full = tag.getBoolean("full");
        ContainerHelper.loadAllItems(tag, inventory);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("full", this.full);
        ContainerHelper.saveAllItems(tag, inventory);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CrockPotBlockEntity crockPot) {
        if (crockPot.isHeated() && crockPot.isFull()) {
            // TODO: cooking here
        }
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = ContainerHelper.removeItem(inventory, slot, count);
        if (!stack.isEmpty()) {
            this.setChanged();
        }

        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    private static Set<ResourceLocation> fillAcceptedFoodTags() {
        ImmutableSet.Builder set = ImmutableSet.builder();
        set.add(Tags.Items.MUSHROOMS);
        return set.build();
    }
}
