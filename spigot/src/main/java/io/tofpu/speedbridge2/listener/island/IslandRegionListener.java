package io.tofpu.speedbridge2.listener.island;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.listener.GameListener;
import io.tofpu.speedbridge2.model.support.worldedit.CuboidRegion;
import io.tofpu.speedbridge2.model.support.worldedit.Vector;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public final class IslandRegionListener extends GameListener {
    private final PlayerService playerService;

    public IslandRegionListener(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler(ignoreCancelled = true) // skipcq: JAVA-W0324
    private void onPlayerMove(final @NotNull PlayerMoveEvent event) {
        final BridgePlayer bridgePlayer = playerService.getIfPresent(event.getPlayer()
                .getUniqueId());
        if (bridgePlayer == null ||!bridgePlayer.isPlaying()) {
            return;
        }

        final GameIsland currentGame = bridgePlayer.getCurrentGame();
        if (currentGame == null) {
            return;
        }

        final CuboidRegion islandRegion = currentGame.region();
        if (islandRegion == null) {
            return;
        }

        final Location location = event.getTo();
        final Vector vector = new Vector(location.getX(), location.getY(), location.getZ());

        final boolean isInRegion = islandRegion.contains(vector);

        if (!isInRegion) {
            currentGame.resetGame();
        }
    }
}
