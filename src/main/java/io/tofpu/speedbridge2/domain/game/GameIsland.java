package io.tofpu.speedbridge2.domain.game;

import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.schematic.IslandPlot;
import io.tofpu.speedbridge2.domain.schematic.SchematicManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class GameIsland {
    private final Island island;
    private final GamePlayer gamePlayer;
    private final IslandPlot islandPlot;

    public static GameIsland of(final Island island, final GamePlayer gamePlayer) {
        return new GameIsland(island, gamePlayer);
    }

    private GameIsland(final Island island, final GamePlayer gamePlayer) {
        this.island = island;
        this.gamePlayer = gamePlayer;

        // setting the player's queue to true
        this.gamePlayer.startQueue();

        this.islandPlot = SchematicManager.INSTANCE.reservePlot(this);

        // reset the player's queue
        this.gamePlayer.resetQueue();
    }

    public void resetGame() {
        this.gamePlayer.teleport(islandPlot);

        gamePlayer.resetBlocks();
        gamePlayer.resetTimer();

        this.gamePlayer.getPlayer().sendMessage("reset the island!");
    }

    public void remove() {
        // TODO: change this
        gamePlayer.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());

        // remove the blocks
        gamePlayer.resetBlocks();

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
