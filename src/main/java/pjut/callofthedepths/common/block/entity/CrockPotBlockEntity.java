package pjut.callofthedepths.common.block.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pjut.callofthedepths.common.item.crafting.CrockPotRecipe;
import pjut.callofthedepths.common.registry.COTDBlockEntities;
import pjut.callofthedepths.common.registry.COTDRecipeTypes;

import java.util.Optional;
import java.util.Set;

public class CrockPotBlockEntity extends BlockEntity implements Container {
    public static final Set<ResourceLocation> acceptedFoodTags = fillAcceptedFoodTags();

    public CrockPotBlockEntity(BlockPos pos, BlockState state) {
        super(COTDBlockEntities.CROCK_POT_BE, pos, state);
    }

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
    private ItemStack result = ItemStack.EMPTY;

    // private LazyOptional<IItemHandler> itemHandler; /* TODO: add capability for exposing inventory */
    private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> new FluidTank(1000, fluidStack -> fluidStack.getFluid().equals(Fluids.WATER)));

    private boolean heated = false;

    public InteractionResult interact(ItemStack interactionItem, Player player, InteractionHand hand) {
        /* Three types of interactions
         *  - Ingredient input / ouput
         *  - Fluid input / output
         *  - Result output
         */
        InteractionResult result;

        LazyOptional<IFluidHandlerItem> fluidHandler = interactionItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if (fluidHandler.isPresent()) {
            result = handleFluidInteraction(interactionItem, player, hand);
        } else if (interactionItem.getItem().equals(Items.BOWL)) {

            interactionItem.split(1);
            player.addItem(new ItemStack(Items.MUSHROOM_STEW));

            result = InteractionResult.SUCCESS;

        } else {
            result = handleIngredientInteraction(interactionItem, player);
        }

        if (result.equals(InteractionResult.SUCCESS)) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
            this.setChanged();
        }

        return result;
    }

    private InteractionResult handleFluidInteraction(ItemStack fluidContainer, Player player, InteractionHand hand) {
        IFluidHandler fluidSource = this.fluidHandler.orElse(new FluidTank(0));
        InvWrapper inventory = new InvWrapper(player.getInventory());

        FluidActionResult actionResult;

        if (this.isFull()) {
            actionResult = FluidUtil.tryFillContainerAndStow(fluidContainer, fluidSource, inventory, 1000, player, true);
        } else {
            actionResult = FluidUtil.tryEmptyContainerAndStow(fluidContainer, fluidSource, inventory, 1000, player, true);
        }

        if (actionResult.isSuccess()) {
            player.setItemInHand(hand, actionResult.getResult());
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @NotNull
    private InteractionResult handleIngredientInteraction(ItemStack interactionItem, Player player) {
        if (!interactionItem.isEmpty()) {   // input ingredient

            Set<ResourceLocation> tags = interactionItem.getItem().getTags();
            Optional<ResourceLocation> sharedTag = acceptedFoodTags.stream().filter(t -> tags.contains(t)).findAny();

            if (sharedTag.isPresent()) {
                System.out.println("shares a tag!");
            }

            System.out.println("adding item to inventory");
            System.out.println("tags " + interactionItem.getItem().getTags());
            for (int slot = 0; slot < inventory.size(); slot++) {
                if (this.getItem(slot).equals(ItemStack.EMPTY)) {
                    this.setItem(slot, interactionItem.split(1));
                    this.setChanged();
                    break;
                }
            }

            return InteractionResult.SUCCESS;
        } else {
            // take out least recently entered item
            for (int slot = inventory.size() - 1; slot >= 0; slot--) {
                if (!this.getItem(slot).equals(ItemStack.EMPTY)) {

                    ItemStack stack = this.getItem(slot);
                    this.setItem(slot, ItemStack.EMPTY);

                    player.addItem(stack);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.FAIL;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CrockPotBlockEntity crockPot) {
        if (crockPot.isHeated() && crockPot.isFull()) {
            Optional<CrockPotRecipe> recipe = level.getRecipeManager().getRecipeFor(COTDRecipeTypes.CROCK_POT, crockPot, level);
            if (recipe.isPresent()) {
                //System.out.println("can make: " + recipe.get().getResultItem());
            }
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY))
            return this.fluidHandler.cast();

        return super.getCapability(cap, side);
    }

    public boolean isFull() {
        IFluidHandler fluidHandler = this.fluidHandler.orElse(new FluidTank(0));
        return fluidHandler.getFluidInTank(0).getAmount() == 1000;
    }

    public boolean isHeated() {
        return heated;
    }

    public void setHeated(boolean heated) {
        this.heated = heated;
    }

    public static void checkHeated(BlockGetter level, BlockPos pos, CrockPotBlockEntity crockPot) {
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
        this.heated = tag.getBoolean("heated");

        this.fluidHandler.ifPresent(h -> {
            ((FluidTank) h).readFromNBT(tag);
        });

        this.inventory.clear();
        ContainerHelper.loadAllItems(tag, inventory);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("heated", this.heated);

        this.fluidHandler.ifPresent(h -> {
            ((FluidTank) h).writeToNBT(tag);
        });

        ContainerHelper.saveAllItems(tag, inventory);
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
    @NotNull
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
        ImmutableSet.Builder<ResourceLocation> set = ImmutableSet.builder();
        set.add(Tags.Items.MUSHROOMS.getName());
        set.add(Tags.Items.MUSHROOMS.getName());
        set.add(Tags.Items.CROPS_BEETROOT.getName());
        set.add(Tags.Items.CROPS_CARROT.getName());
        return set.build();
    }
}
