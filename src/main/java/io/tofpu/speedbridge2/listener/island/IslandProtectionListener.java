package io.tofpu.speedbridge2.listener.island;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockBreakEventWrapper;
import io.tofpu.speedbridge2.listener.wrapper.wrappers.BlockPlaceEventWrapper;
import io.tofpu.speedbridge2.support.worldedit.CuboidRegion;
import io.tofpu.speedbridge2.support.worldedit.Vector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class IslandProtectionListener extends GameListener {
    @EventHandler(ignoreCancelled = true)
    private void onBlockBreak(final @NotNull BlockBreakEventWrapper eventWrapper) {
        final BlockBreakEvent event = eventWrapper.getEvent();

        final GamePlayer gamePlayer = eventWrapper.getGamePlayer();
        final Block block = event.getBlock();

        // if the player haven't placed this block, return
        event.setCancelled(true);
        if (gamePlayer.hasPlaced(block)) {
            gamePlayer.removeBlock(block);
            block.setType(Material.AIR);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockPlaceEvent(final @NotNull BlockPlaceEventWrapper eventWrapper) {
        final GameIsland gameIsland = eventWrapper.getCurrentGame();
        final CuboidRegion region = gameIsland.getIslandPlot().region();

        final BlockPlaceEvent event = eventWrapper.getEvent();
        final Location location = event.getBlockPlaced().getLocation();
        final boolean isInRegion = region.contains(new Vector(location.getX(), location.getY(), location.getZ()));

        // if the block placement was outside of the island's region, prevent the block placement
        if (!isInRegion) {
            event.setCancelled(true);
            return;
        }

        eventWrapper.getGamePlayer().addBlock(event.getBlockPlaced());
    }
}
