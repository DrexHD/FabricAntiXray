package me.drex.antixray.common.util.controller;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.function.IntSupplier;

public class ObfuscateLayerChunkPacketBlockController extends ObfuscateChunkPacketBlockController {

    public ObfuscateLayerChunkPacketBlockController(Level level, Set<Block> replacementBlocks, Set<Block> hiddenBlocks, int maxBlockHeight, int updateRadius, boolean lavaObscures, boolean usePermission) {
        super(level, replacementBlocks, hiddenBlocks, maxBlockHeight, updateRadius, lavaObscures, usePermission);
    }

    @Override
    public IntSupplier layerIntSupplier(int numberOfBlocks) {
        // Get ONE random int per layer
        int result = super.layerIntSupplier(numberOfBlocks).getAsInt();
        return () -> result;
    }

}
