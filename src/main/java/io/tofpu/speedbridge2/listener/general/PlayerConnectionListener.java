package io.tofpu.speedbridge2.listener.general;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AutoRegister
public final class PlayerConnectionListener extends GameListener {
    final PlayerService playerService = PlayerService.INSTANCE;

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        // internally refreshing the BridgePlayer object, to avoid the Player object
        // from breaking
        playerService.internalRefresh(event.getPlayer());
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        // invalidate
        playerService.invalidate(event.getPlayer());
    }
}
