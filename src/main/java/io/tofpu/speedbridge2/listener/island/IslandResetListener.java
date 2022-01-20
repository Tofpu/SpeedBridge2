package io.tofpu.speedbridge2.listener.island;

import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public final class IslandResetListener extends GameListener {
    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying()) {
            return;
        }

        // quit from the game
        gamePlayer.getCurrentGame().getIsland().leaveGame(gamePlayer);
    }
}
