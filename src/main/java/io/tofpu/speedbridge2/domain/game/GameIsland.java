package io.tofpu.speedbridge2.domain.game;

import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.schematic.SchematicManager;
import org.bukkit.Bukkit;

public final class GameIsland {
    private final Island island;
    private final GamePlayer gamePlayer;

    public GameIsland(final Island island, final GamePlayer gamePlayer) {
        this.island = island;
        this.gamePlayer = gamePlayer;

        // setting the player's queue to true
        this.gamePlayer.startQueue();

        SchematicManager.INSTANCE.reservePlot(this);
    }

    public void leaveGame() {
        // TODO: change this
        gamePlayer.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());

        // free the plot
        SchematicManager.INSTANCE.freePlot(this);

        // set the player's game to -1, as they're leaving the island
        gamePlayer.setIslandSlot(-1);
    }

    public Island getIsland() {
        return island;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
}
