package io.tofpu.speedbridge2.listener.island;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.domain.schematic.IslandPlot;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class IslandRegionListener extends GameListener {
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying()) {
            return;
        }
        final GameIsland gameIsland = gamePlayer.getCurrentGame();
        final Region region = gameIsland.getIsland().getSchematicClipboard().getRegion();

        final Location location = event.getTo();
        final boolean isInRegion = region.contains(new Vector(location.getX(), location.getY(), location.getZ()));

        // if the player is not in the region, teleport them back to the island location
        if (!isInRegion) {
            final IslandPlot islandPlot = gameIsland.getIslandPlot();
            event.setTo(new Location(islandPlot.getWorld(), islandPlot.getX(), islandPlot.getY(), islandPlot.getZ()));
        }
    }
}
