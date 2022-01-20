package io.tofpu.speedbridge2.listener.island;

import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.service.PlayerService;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public final class IslandResetListener extends GameListener {
    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.remove(event.getPlayer()
                .getUniqueId());
        if (!bridgePlayer.isPlaying()) {
            return;
        }

        // quit from the game
        bridgePlayer.getGamePlayer().getCurrentGame().getIsland().leaveGame(bridgePlayer);
    }
}
