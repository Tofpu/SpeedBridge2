package com.github.tofpu.speedbridge2.bukkit.bootstrap.game;

import com.github.tofpu.speedbridge2.bukkit.helper.CoreConversionHelper;
import com.github.tofpu.speedbridge2.common.game.BridgeSystem;
import com.github.tofpu.speedbridge2.common.game.IslandGameStates;
import com.github.tofpu.speedbridge2.common.game.IslandGameData;
import com.github.tofpu.speedbridge2.object.Position;
import io.github.tofpu.speedbridge.gameengine.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class GameListener implements Listener {
    private final BridgeSystem bridgeSystem;

    public GameListener(BridgeSystem bridgeSystem) {
        this.bridgeSystem = bridgeSystem;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void on(final PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!bridgeSystem.isInGame(playerId)) {
            return;
        }
        bridgeSystem.leaveGame(playerId);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void on(final BlockPlaceEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        Game<IslandGameData> game = bridgeSystem.getGameByPlayer(playerId);
        if (game == null) return;

        IslandGameData gameData = game.data();
        if (!gameData.hasTimerBegun()) {
            gameData.beginTimer(true);
        }

        gameData.addBlock(CoreConversionHelper.toPosition(event.getBlockPlaced().getLocation()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void on(final BlockBreakEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        Game<IslandGameData> game = bridgeSystem.getGameByPlayer(playerId);
        if (game == null) return;

        Position blockPosition = CoreConversionHelper.toPosition(event.getBlock().getLocation());
        IslandGameData gameData = game.data();
        if (!gameData.hasPlacedBlockAt(blockPosition)) {
            event.setCancelled(true);
            return;
        }

        gameData.removeBlock(blockPosition);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void on(final PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return;

        UUID playerId = event.getPlayer().getUniqueId();
        Game<IslandGameData> game = bridgeSystem.getGameByPlayer(playerId);
        if (game == null || !game.data().hasTimerBegun()) return;

        game.dispatch(IslandGameStates.SCORED);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void on(final PlayerMoveEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        Game<IslandGameData> game = bridgeSystem.getGameByPlayer(playerId);
        if (game == null) return;

        IslandGameData gameData = game.data();
        boolean isInRegion = gameData.getLand().isInsideRegion(CoreConversionHelper.toVector(event.getTo().toVector()));
        if (!isInRegion) {
            game.dispatch(IslandGameStates.RESET);
        }
    }
}