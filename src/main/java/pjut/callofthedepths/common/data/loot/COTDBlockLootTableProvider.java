package pjut.callofthedepths.common.data.loot;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
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
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        System.out.println("called get known");
        List<Block> known = new LinkedList<>();
        known.add(COTDBlocks.ROPE_BLOCK.get());
        return known;
    }
}
