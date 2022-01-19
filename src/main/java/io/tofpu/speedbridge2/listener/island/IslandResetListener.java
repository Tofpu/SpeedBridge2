package io.tofpu.speedbridge2.listener.island;

import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.domain.misc.Score;
import io.tofpu.speedbridge2.domain.service.PlayerService;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class IslandResetListener extends GameListener {
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
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying() || !gamePlayer.hasTimerStarted()) {
            return;
        }

        // TODO: if the player interacted with a pressure plate,
        //  a timer is active, reset the island & update the score
        //  accordingly

        final Island island = gamePlayer.getCurrentGame().getIsland();
        final long startedAt = gamePlayer.getTimer();
                final long capturedAt = System.nanoTime();

//        final Instant instant = Instant.now().minusNanos(startedAt);
        final long seconds = TimeUnit.NANOSECONDS.toSeconds(capturedAt - startedAt);
        final Score score = new Score(island.getSlot(), seconds);

        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(player.getUniqueId());
        bridgePlayer.setScoreIfLower(island.getSlot(), score.getScore());

        player.sendMessage("you scored: " + seconds);

        gamePlayer.getCurrentGame().resetGame();
    }
}
