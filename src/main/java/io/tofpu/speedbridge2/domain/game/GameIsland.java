package io.tofpu.speedbridge2.domain.game;

import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.schematic.IslandPlot;
import io.tofpu.speedbridge2.domain.schematic.SchematicManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public void onJoin() {
        final Player player = gamePlayer.getBridgePlayer().getPlayer();

        player.getInventory().clear();
        player.getInventory().setItem(0, new ItemStack(Material.WOOL,
                64));

        player.setHealth(20);
        player.setFoodLevel(20);

        player.setGameMode(GameMode.SURVIVAL);
    }

    public void resetGame() {
        gamePlayer.resetBlocks();
        gamePlayer.resetTimer();

        this.gamePlayer.teleport(islandPlot);

        gamePlayer.getBridgePlayer().getPlayer().getInventory().setItem(0,
                new ItemStack(Material.WOOL,
                64));

        this.gamePlayer.getBridgePlayer().getPlayer().sendMessage("the island has been reset");
    }

    public void remove() {
        final Player player = gamePlayer.getBridgePlayer().getPlayer();

        // TODO: change this
        player.getInventory().clear();
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());

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
