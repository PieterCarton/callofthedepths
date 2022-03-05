package pjut.callofthedepths.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import pjut.callofthedepths.common.registry.COTDBlockEntities;

public class CrockPotBlockEntity extends BlockEntity {
    public CrockPotBlockEntity(BlockPos pos, BlockState state) {
        super(COTDBlockEntities.CROCK_POT_BE, pos, state);
    }

    public void interact(ItemStack interactionItem) {
        System.out.println("interaction propogated to block entity");
        System.out.println("clicked on crock pot with: " + interactionItem.toString());

        LazyOptional<IFluidHandlerItem> fluidHandler = interactionItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        if (fluidHandler.isPresent()) {
            System.out.println("item is a fluid handler containing");
            fluidHandler.ifPresent(h -> System.out.println(h.getFluidInTank(0).getFluid().toString()));
        }
    }
}
