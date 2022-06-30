package pjut.callofthedepths.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;

public class DebugItem extends Item {
    public DebugItem(Item.Properties sharedProperties) {
        super(sharedProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            BiomeSource biomeSource = ((ServerLevel)level).getChunkSource().getGenerator().getBiomeSource();

            // for (Biome b: biomeSource.possibleBiomes()) {
            //    System.out.println(b.getRegistryName().toString());
            //}
        }
        return super.use(level, player, hand);
    }
}
