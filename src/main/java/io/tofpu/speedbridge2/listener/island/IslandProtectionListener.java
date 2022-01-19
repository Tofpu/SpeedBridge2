package io.tofpu.speedbridge2.listener.island;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public final class IslandProtectionListener extends GameListener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying()) {
            return;
        }
        final Block block = event.getBlock();

        // if the player haven't placed this block, return
        if (!gamePlayer.hasPlaced(block)) {
            return;
        }

        gamePlayer.removeBlock(block);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlaceEvent(final BlockPlaceEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying()) {
            return;
        }
        final GameIsland gameIsland = gamePlayer.getCurrentGame();
        final Region region = gameIsland.getIslandPlot().region();

        System.out.println("location: " + region.toString());

        final Location location = event.getBlockPlaced().getLocation();
        final boolean isInRegion = region.contains(new Vector(location.getX(), location.getY(), location.getZ()));

        // if the block placement was outside of the island's region, prevent the block placement
        if (!isInRegion) {
            event.setCancelled(true);
            return;
        }

        gamePlayer.addBlock(event.getBlockPlaced());
    }
}
