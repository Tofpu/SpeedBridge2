package com.github.tofpu.speedbridge2.bootstrap.game;

import com.github.tofpu.speedbridge2.adapter.BukkitAdapter;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.object.Position;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class GameListener implements Listener {
    private final IslandGameHandler gameHandler;
    private final OngoingGameRegistry gameRegistry;

    public GameListener(IslandGameHandler gameHandler, OngoingGameRegistry gameRegistry) {
        this.gameHandler = gameHandler;
        this.gameRegistry = gameRegistry;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void on(final BlockPlaceEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        IslandGame game = gameRegistry.get(playerId);
        if (game == null) return;

        if (!game.hasTimerBegun()) {
            game.beginTimer(true);
        }

        game.addBlock(BukkitAdapter.toPosition(event.getBlockPlaced().getLocation()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void on(final BlockBreakEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        IslandGame game = gameRegistry.get(playerId);
        if (game == null) return;

        Position blockPosition = BukkitAdapter.toPosition(event.getBlock().getLocation());
        if (!game.hasPlacedBlockAt(blockPosition)) {
            event.setCancelled(true);
            return;
        }

        game.removeBlock(blockPosition);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void on(final PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return;

        UUID playerId = event.getPlayer().getUniqueId();
        IslandGame game = gameRegistry.get(playerId);
        if (game == null || !game.hasTimerBegun()) return;

        gameHandler.scoredGame(playerId);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void on(final PlayerMoveEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        IslandGame game = gameRegistry.get(playerId);
        if (game == null) return;

        boolean isInRegion = game.getLand().isInsideRegion(BukkitAdapter.toVector(event.getTo().toVector()));
        if (!isInRegion) {
            gameHandler.resetGame(playerId);
        }
    }
}