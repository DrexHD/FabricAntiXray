package me.drex.antixray.util;

import me.drex.antixray.interfaces.ILevel;
import me.drex.antixray.util.controller.ChunkPacketBlockController;
import me.drex.antixray.util.controller.DisabledChunkPacketBlockController;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

public final class Util {

    public static ChunkPacketBlockController getBlockController(Level level) {
        return ((ILevel) level).getChunkPacketBlockController();
    }

    public static ChunkPacketBlockController getBlockController(ServerPlayer player) {
        ChunkPacketBlockController controller = getBlockController(player.level());
        return controller.shouldModify(player) ? controller : DisabledChunkPacketBlockController.NO_OPERATION_INSTANCE;
    }

    // Converts height accessors from ChunkAccess into levels.
    public static Level getLevel(LevelHeightAccessor heightAccessor) {
        if (heightAccessor instanceof Level level) {
            return level;
        } else if (heightAccessor instanceof LevelChunk levelChunk) {
            return levelChunk.getLevel();
        } else if (heightAccessor instanceof ChunkAccess chunk) {
            return getLevel(chunk.levelHeightAccessor);
        } else {
            // If we return null, chunks will not get properly obfuscated.
            throw new IllegalStateException("Failed to add block presets as height accessor was an instance of " + heightAccessor.getClass().getSimpleName());
        }
    }
}
