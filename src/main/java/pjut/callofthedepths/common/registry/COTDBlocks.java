package pjut.callofthedepths.common.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pjut.callofthedepths.common.block.CrockPotBlock;
import pjut.callofthedepths.common.block.RopeBlock;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

import java.util.function.Supplier;

public class COTDBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CallOfTheDepths.MOD_ID);

    public static final RegistryObject<Block> ROPE_BLOCK = register("rope_block",
            () -> new RopeBlock(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<Block> SILKEN_HUSK = registerWithItem("silken_husk",
            () -> new Block(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.WOOL)));

    public static final RegistryObject<Block> GYPSUM = registerWithItem("gypsum",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> GYPSUM_STAIRS = registerWithItem("gypsum_stairs",
            () -> new StairBlock(() -> GYPSUM.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> GYPSUM_SLAB = registerWithItem("gypsum_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final RegistryObject<Block> POLISHED_GYPSUM = registerWithItem("polished_gypsum",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> POLISHED_GYPSUM_STAIRS = registerWithItem("polished_gypsum_stairs",
            () -> new StairBlock(() -> POLISHED_GYPSUM.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> POLISHED_GYPSUM_SLAB = registerWithItem("polished_gypsum_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final RegistryObject<Block> WEB_CARPET = registerWithItem("web_carpet",
            () -> new MultifaceBlock(BlockBehaviour.Properties.of(Material.WEB).requiresCorrectToolForDrops().strength(1.5F, 6.0F).jumpFactor(0.8f).speedFactor(0.2f).noOcclusion().noCollission()));

    public static final RegistryObject<Block> CROCK_POT = registerWithItem("crock_pot",
            () -> new CrockPotBlock(BlockBehaviour.Properties.of(Material.STONE)));

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static RegistryObject<Block> register(String name, Supplier<Block> supplier) {
        return BLOCKS.register(name, supplier);
    }

    public static RegistryObject<Block> registerWithItem(String name, Supplier<Block> supplier) {
        RegistryObject<Block> registryObject = BLOCKS.register(name, supplier);
        COTDItems.ITEMS.register(name, () -> new BlockItem(registryObject.get(), COTDItems.getSharedProperties()));
        return registryObject;
    }
}
