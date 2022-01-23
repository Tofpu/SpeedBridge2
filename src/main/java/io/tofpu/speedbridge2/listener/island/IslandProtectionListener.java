package io.tofpu.speedbridge2.listener.island;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;
import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.domain.service.PlayerService;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

@AutoRegister
public final class IslandProtectionListener extends GameListener {
    @EventHandler(ignoreCancelled = true)
    private void onBlockBreak(final BlockBreakEvent event) {
        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(event.getPlayer()
                .getUniqueId());
        if (!bridgePlayer.isPlaying()) {
            return;
        }
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();
        final Block block = event.getBlock();

        // if the player haven't placed this block, return
        if (!gamePlayer.hasPlaced(block)) {
            event.setCancelled(true);
            return;
        }

        gamePlayer.removeBlock(block);
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockPlaceEvent(final BlockPlaceEvent event) {
        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(event.getPlayer()
                .getUniqueId());
        if (!bridgePlayer.isPlaying()) {
            return;
        }
        final GamePlayer gamePlayer = bridgePlayer.getGamePlayer();
        final GameIsland gameIsland = gamePlayer.getCurrentGame();
        final Region region = gameIsland.getIslandPlot().region();

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
