package io.tofpu.speedbridge2.domain.game;

import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.schematic.SchematicManager;
import io.tofpu.speedbridge2.domain.schematic.IslandPlot;
import org.bukkit.Bukkit;

public final class GameIsland {
    private final Island island;
    private final GamePlayer gamePlayer;
    private final IslandPlot islandPlot;

    public GameIsland(final Island island, final GamePlayer gamePlayer) {
        this.island = island;
        this.gamePlayer = gamePlayer;

        // setting the player's queue to true
        this.gamePlayer.startQueue();

        this.islandPlot = SchematicManager.INSTANCE.reservePlot(this);

        // reset the player's queue
        this.gamePlayer.resetQueue();
    }

    public void leaveGame() {
        // TODO: change this
        gamePlayer.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());

        // free the plot
        SchematicManager.INSTANCE.freePlot(this);

        // set the player's game to null, as they're leaving the island
        gamePlayer.setCurrentGame(null);
    }

    public Island getIsland() {
        return island;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public IslandPlot getIslandPlot() {
        return islandPlot;
    }
}
