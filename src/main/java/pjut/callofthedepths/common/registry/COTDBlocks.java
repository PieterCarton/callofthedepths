package pjut.callofthedepths.common.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pjut.callofthedepths.common.block.RopeBlock;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

import java.util.Properties;
import java.util.function.Supplier;

public class COTDBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CallOfTheDepths.MOD_ID);

    public static final RegistryObject<Block> ROPE_BLOCK = register("rope_block",
            () -> new RopeBlock(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.WOOL).noOcclusion()));

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static RegistryObject<Block> register(String name, Supplier<Block> supplier) {
        return BLOCKS.register(name, supplier);
    }
}