package me.drex.antixray.forge;

import me.drex.antixray.AntiXray;
import me.drex.antixray.util.Platform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

import java.nio.file.Path;

public class AntiXrayForge extends AntiXray {

    public static final PermissionNode<Boolean> ANTIXRAY_BYPASS = new PermissionNode<>(MOD_ID, "bypass", PermissionTypes.BOOLEAN, (player, playerUUID, context) -> false);

    public AntiXrayForge() {
        super(Platform.FORGE);
    }

    @Override
    public boolean canBypassXray(ServerPlayer player) {
        return PermissionAPI.getPermission(player, ANTIXRAY_BYPASS);
    }

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
