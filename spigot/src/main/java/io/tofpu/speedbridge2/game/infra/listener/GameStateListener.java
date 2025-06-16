package io.tofpu.speedbridge2.game.infra.listener;

import io.tofpu.speedbridge2.arena.CuboidRegion;
import io.tofpu.speedbridge2.game.GamePlayer;
import io.tofpu.speedbridge2.game.service.GameService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

import static io.tofpu.speedbridge2.util.LocationUtil.asVector;

public class GameStateListener implements Listener {
    private final GameService gameService;

    public GameStateListener(GameService gameService) {
        this.gameService = gameService;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void resetGameWhenPlayerGoesOutsideIslandBorders(PlayerMoveEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        gameService.game(playerId).ifPresent(game -> {
            CuboidRegion region = game.region();
            if (!region.contains(asVector(event.getTo()))) {
                gameService.resetGame(game);
            }
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void startTimerWhenFirstBlockIsPlaced(BlockPlaceEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        gameService.game(playerId).ifPresent(game -> {
            GamePlayer gamePlayer = game.gamePlayer();
            if (gamePlayer.hasTimerStarted()) {
                return;
            }
            gamePlayer.startTimer();
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void addScoreWhenPlayerTriggersAPressurePlate(PlayerInteractEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        gameService.game(playerId).ifPresent(game -> {
            // making sure that the player interacted with a pressure plate
            if (event.getAction() != Action.PHYSICAL
                    || !event.getClickedBlock().getType().name().contains("PLATE")) {
                return;
            }

            GamePlayer gamePlayer = game.gamePlayer();
            if (!gamePlayer.hasTimerStarted()) {
                return;
            }
            gamePlayer.endTimer();
            gameService.addScore(game);
        });
    }
}
