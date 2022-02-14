package io.tofpu.speedbridge2.domain.island.object.extra;

import io.tofpu.speedbridge2.domain.common.config.category.LobbyCategory;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.MessageUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import io.tofpu.speedbridge2.domain.island.schematic.SchematicManager;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStatType;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class GameIsland {
    private static final String STYLE =
            "<gold>" + MessageUtil.Symbols.WARNING.getSymbol() + "<yellow> ";
    private static final String ISLAND_RESET = STYLE + "The island has been reset!";

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
        player.getInventory()
                .setItem(0, new ItemStack(gamePlayer.getBridgePlayer()
                        .getChoseMaterial(), 64));

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        player.setGameMode(GameMode.SURVIVAL);
    }

    public void resetGame(final boolean notify) {
        gamePlayer.resetBlocks();
        gamePlayer.resetTimer();
        gamePlayer.teleport(islandPlot);

        gamePlayer.getBridgePlayer().increment(PlayerStatType.TOTAL_TRIES);

        final Player player = gamePlayer.getBridgePlayer().getPlayer();

        player.getInventory().setItem(0,
                new ItemStack(gamePlayer.getBridgePlayer()
                        .getChoseMaterial(),
                        64));

        if (notify) {
            BridgeUtil.sendMessage(player, ISLAND_RESET);
        }
    }

    public void resetGame() {
        resetGame(true);
    }

    public void remove() {
        final Player player = gamePlayer.getBridgePlayer()
                .getPlayer();

        if (player != null) {
            player.getInventory()
                    .clear();

            final LobbyCategory lobbyCategory = ConfigurationManager.INSTANCE.getLobbyCategory();

            // teleport the player to the lobby location
            final Location location = lobbyCategory.getLobbyLocation();
            if (location != null) {
                player.teleport(location);
            }
        }

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
