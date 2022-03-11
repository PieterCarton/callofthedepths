package pjut.callofthedepths.common.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import pjut.callofthedepths.common.block.entity.CrockPotBlockEntity;
import pjut.callofthedepths.common.CallOfTheDepths;

@Mod.EventBusSubscriber(modid = CallOfTheDepths.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class COTDBlockEntities {

    public static BlockEntityType<CrockPotBlockEntity> CROCK_POT_BE;

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.BlockEntitySupplier<T> blockEntity, IForgeRegistry<BlockEntityType<?>> registry, Block... validBlocks) {
        BlockEntityType<T> type = BlockEntityType.Builder.of(blockEntity, validBlocks).build(null);
        type.setRegistryName(CallOfTheDepths.MOD_ID, name);
        registry.register(type);
        return type;
    }

    @SubscribeEvent
    public static void onRegisterBlockEntities(final RegistryEvent.Register<BlockEntityType<?>> evt) {
        System.out.println("registering entities");
        CROCK_POT_BE = register("crock_pot_be", CrockPotBlockEntity::new, evt.getRegistry(), COTDBlocks.CROCK_POT.get());
    }
}
