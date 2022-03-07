package pjut.callofthedepths.common.data.loot;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import pjut.callofthedepths.common.registry.COTDBlocks;

import java.util.LinkedList;
import java.util.List;

public class COTDBlockLootTableProvider extends BlockLoot {

    public String getName() {
        return "COTD Block Loot Table Provider";
    }

    @Override
    protected void addTables() {
        dropSelf(COTDBlocks.ROPE_BLOCK.get());
        dropSelf(COTDBlocks.GYPSUM.get());
        dropSelf(COTDBlocks.CROCK_POT.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> known = new LinkedList<>();

        known.add(COTDBlocks.ROPE_BLOCK.get());
        known.add(COTDBlocks.GYPSUM.get());
        known.add(COTDBlocks.CROCK_POT.get());

        return known;
    }
}
