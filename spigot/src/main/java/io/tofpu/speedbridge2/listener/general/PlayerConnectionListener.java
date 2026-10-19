package io.tofpu.speedbridge2.listener.general;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.config.category.LobbyCategory;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.common.util.UpdateChecker;
import io.tofpu.speedbridge2.model.player.PlayerService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class PlayerConnectionListener extends GameListener {
    private final PlayerService playerService;

    public PlayerConnectionListener(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler(priority = EventPriority.LOWEST) // skipcq: JAVA-W0324
    private void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        // if the bridge player instance is loaded, then refresh the player instance
        playerService.loadIfAbsent(player, (bridgePlayer) -> playerService.internalRefresh(player, bridgePlayer));

        if (player.isOp()) {
            if (UpdateChecker.isInitialized() && ConfigurationManager.INSTANCE.getGeneralCategory().shouldCheckForUpdates()) {
                UpdateChecker.get().updateNotification(player);
            }
        }

        teleportToLobby(player);
    }

    private void teleportToLobby(final Player player) {
        final LobbyCategory lobbyCategory =
                ConfigurationManager.INSTANCE.getLobbyCategory();
        final Location location = lobbyCategory.getLobbyLocation();

        // a lobby is required to utilize speedbridge2 features
        if (location == null) {
            if (player.isOp()) {
                BridgeUtil.sendMessage(player, Message.INSTANCE.lobbyMissing);
            }
            return;
        }

        if (!lobbyCategory.isTeleportOnJoin()) {
            return;
        }

        player.teleport(location);

        if (lobbyCategory.clearInventoryAfterTeleport() && player.getWorld().equals(location.getWorld())) {
            // clears the player's inventory. in-case the PlayerQuitEvent missed it.
            player.getInventory().clear();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // skipcq: JAVA-W0324
    private void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        playerService.invalidate(player);
    }
}