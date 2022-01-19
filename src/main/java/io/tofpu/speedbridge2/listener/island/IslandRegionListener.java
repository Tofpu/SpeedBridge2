package io.tofpu.speedbridge2.listener.island;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import io.tofpu.speedbridge2.domain.game.GameIsland;
import io.tofpu.speedbridge2.domain.game.GamePlayer;
import io.tofpu.speedbridge2.domain.schematic.IslandPlot;
import io.tofpu.speedbridge2.listener.GameListener;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public final class IslandRegionListener extends GameListener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(final PlayerMoveEvent event) {
        final GamePlayer gamePlayer = GamePlayer.of(event.getPlayer());
        if (!gamePlayer.isPlaying()) {
            return;
        }
        final GameIsland gameIsland = gamePlayer.getCurrentGame();
        final IslandPlot islandPlot = gameIsland.getIslandPlot();
        final Region region = islandPlot.region();

        final Location location = event.getTo();
        final Vector vector = new Vector(location.getX(), location.getY(), location.getZ());

        final boolean isInRegion = CuboidRegion.makeCuboid(region).contains(vector);

        if (!isInRegion) {
            gameIsland.resetGame();
        }
    }
}
