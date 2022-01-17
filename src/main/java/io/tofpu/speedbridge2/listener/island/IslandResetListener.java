package io.tofpu.speedbridge2.listener.island;

import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class IslandResetListener extends GameListener {
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying()) {
            return;
        }

        // quit from the game
        gamePlayer.getCurrentGame().getIsland().leaveGame(gamePlayer);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying()) {
            return;
        }

        // TODO: if the player interacted with a pressure plate,
        //  a timer is active, reset the island & update the score
        //  accordingly
    }
}
