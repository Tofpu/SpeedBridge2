package io.tofpu.speedbridge2.listener.general;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.config.category.LobbyCategory;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.setup.IslandSetupManager;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class PlayerConnectionListener extends GameListener {
    final PlayerService playerService = PlayerService.INSTANCE;

    @EventHandler
    private void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        // internally refreshing the BridgePlayer object, to avoid the Player object
        // from breaking
        final Player player = event.getPlayer();
        playerService.internalRefresh(player);

        final LobbyCategory lobbyCategory =
                ConfigurationManager.INSTANCE.getLobbyCategory();

        // if teleport_on_join is set to true, teleport the player to the lobby location
        if (lobbyCategory.isTeleportOnJoin()) {
            final Location location = lobbyCategory.getLobbyLocation();
            if (location != null) {
                player.teleport(location);
                return;
            }

            BridgeUtil.sendMessage(player, Message.INSTANCE.LOBBY_MISSING);
        }
    }

    @EventHandler
    private void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        playerService.invalidate(player);
        IslandSetupManager.INSTANCE.invalidate(player.getUniqueId());
    }
}
