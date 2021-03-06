package io.tofpu.speedbridge2.listener.island;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockBreakEventWrapper;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockPlaceEventWrapper;
import io.tofpu.speedbridge2.model.support.worldedit.CuboidRegion;
import io.tofpu.speedbridge2.model.support.worldedit.Vector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class IslandProtectionListener extends GameListener {
    @EventHandler(ignoreCancelled = true) // skipcq: JAVA-W0324
    private void onBlockBreak(final @NotNull BlockBreakEventWrapper eventWrapper) {
        final BlockBreakEvent event = eventWrapper.getEvent();

        final GamePlayer gamePlayer = eventWrapper.getGamePlayer();
        final Block block = event.getBlock();

        event.setCancelled(true);
        if (gamePlayer.hasPlaced(block)) {
            gamePlayer.removeBlock(block);
            block.setType(Material.AIR);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockPlaceEvent(final @NotNull BlockPlaceEventWrapper eventWrapper) {
        final GamePlayer gamePlayer = eventWrapper.getGamePlayer();
        final GameIsland gameIsland = eventWrapper.getCurrentGame();

        final CuboidRegion region = gameIsland.getIslandPlot().region();

        final BlockPlaceEvent event = eventWrapper.getEvent();
        final Location location = event.getBlockPlaced().getLocation();
        final boolean inBounds = region.contains(new Vector(location.getX(), location.getY(), location.getZ()));

        // if the block placement was outside the island's region bounds, prevent the
        // block placement
        if (!inBounds) {
            event.setCancelled(true);
            return;
        }

        gamePlayer.addBlock(event.getBlockPlaced());
    }
}
