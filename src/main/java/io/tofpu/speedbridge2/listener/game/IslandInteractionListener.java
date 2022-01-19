package io.tofpu.speedbridge2.listener.game;

import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public final class IslandInteractionListener extends GameListener {
    @EventHandler
    private void onBlockPlace(final BlockPlaceEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying() || gamePlayer.hasTimerStarted()) {
            return;
        }

        gamePlayer.startTimer();
        event.getPlayer().sendMessage("started timer!");
    }
}
