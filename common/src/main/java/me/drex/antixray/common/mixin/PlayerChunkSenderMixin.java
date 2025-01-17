package me.drex.antixray.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.drex.antixray.common.interfaces.IClientboundChunkBatchStartPacket;
import me.drex.antixray.common.util.Arguments;
import net.minecraft.network.protocol.game.ClientboundChunkBatchStartPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(PlayerChunkSender.class)
public abstract class PlayerChunkSenderMixin {

    @Redirect(
        method = "sendNextChunks",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/network/protocol/game/ClientboundChunkBatchStartPacket;INSTANCE:Lnet/minecraft/network/protocol/game/ClientboundChunkBatchStartPacket;"
        )
    )
    private ClientboundChunkBatchStartPacket addBatchSizeArgument(
        @Local List<LevelChunk> list, @Share("startPacket") LocalRef<IClientboundChunkBatchStartPacket> ipacketRef
    ) {
        ClientboundChunkBatchStartPacket startPacket = ClientboundChunkBatchStartPacketAccessor.init();
        IClientboundChunkBatchStartPacket iPacket = (IClientboundChunkBatchStartPacket) startPacket;
        iPacket.antixray$setBatchSize(list.size());
        ipacketRef.set(iPacket);
        return startPacket;
    }

    @WrapOperation(
        method = "sendNextChunks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/PlayerChunkSender;sendChunk(Lnet/minecraft/server/network/ServerGamePacketListenerImpl;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/LevelChunk;)V"
        )
    )
    private void setBatchStartPacketArgument(
        ServerGamePacketListenerImpl serverGamePacketListenerImpl, ServerLevel serverLevel, LevelChunk levelChunk,
        Operation<Void> original, @Share("startPacket") LocalRef<IClientboundChunkBatchStartPacket> ipacketRef
    ) {
        // Pass the batch start packet to the chunk packets
        var previous0 = Arguments.BATCH_START_PACKET.get();
        var previous1 = Arguments.PACKET_LISTENER.get();
        Arguments.BATCH_START_PACKET.set(ipacketRef.get());
        Arguments.PACKET_LISTENER.set(serverGamePacketListenerImpl);
        try {
            original.call(serverGamePacketListenerImpl, serverLevel, levelChunk);
        } finally {
            Arguments.BATCH_START_PACKET.set(previous0);
            Arguments.PACKET_LISTENER.set(previous1);
        }
    }

}
