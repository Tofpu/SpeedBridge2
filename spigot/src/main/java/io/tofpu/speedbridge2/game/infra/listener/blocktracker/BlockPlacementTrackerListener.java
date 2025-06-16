package io.tofpu.speedbridge2.game.infra.listener.blocktracker;

import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.arena.CuboidRegion;
import io.tofpu.speedbridge2.game.GamePlayer;
import io.tofpu.speedbridge2.game.event.GameResetEvent;
import io.tofpu.speedbridge2.game.domain.Game;
import io.tofpu.speedbridge2.game.event.GameScoreEvent;
import io.tofpu.speedbridge2.game.service.GameService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

import static io.tofpu.speedbridge2.util.LocationUtil.asVector;

public class BlockPlacementTrackerListener implements Listener {
    private final GameService gameService;

    public BlockPlacementTrackerListener(GameService gameService) {
        this.gameService = gameService;
    }

    @SubscribeEvent
    public void onGameReset(GameResetEvent event) {
        Game game = event.getGame();
        if (game == null) {
            return;
        }

        // Clear the placed blocks when the game is reset
        clearPlacedBlocks(game);
    }

    @SubscribeEvent
    public void onGameScore(GameScoreEvent event) {
        Game game = event.getGame();
        if (game == null) {
            return;
        }

        // Clear the placed blocks when the game is scored
        clearPlacedBlocks(game);
    }

    private static void clearPlacedBlocks(Game game) {
        GamePlayer gamePlayer = game.gamePlayer();
        for (Location placedBlock : gamePlayer.placedBlocks()) {
            placedBlock.getBlock().setType(Material.AIR);
        }
        gamePlayer.placedBlocks().clear();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockPlace(BlockBreakEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        Game game = gameService.getGame(playerId);
        if (game == null) {
            return;
        }

        CuboidRegion region = game.region();
        if (!region.contains(asVector(event.getBlock().getLocation()))) {
            event.setCancelled(true);
            return;
        }

        game.gamePlayer().addPlacedBlock(event.getBlock().getLocation());
    }
}
