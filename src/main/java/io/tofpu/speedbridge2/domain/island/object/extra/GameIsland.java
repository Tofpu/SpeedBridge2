package io.tofpu.speedbridge2.domain.island.object.extra;

import io.tofpu.speedbridge2.domain.common.Message;
import io.tofpu.speedbridge2.domain.common.config.category.LobbyCategory;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.umbrella.GameIslandUmbrella;
import io.tofpu.speedbridge2.domain.island.plot.IslandPlot;
import io.tofpu.speedbridge2.domain.island.schematic.SchematicManager;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStatType;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.support.worldedit.CuboidRegion;
import io.tofpu.umbrella.domain.Umbrella;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class GameIsland {
    private final Umbrella umbrella;
    private final Island island;
    private final GamePlayer gamePlayer;
    private IslandPlot islandPlot;

    public static GameIsland of(final Island island, final GamePlayer gamePlayer) {
        return new GameIsland(island, gamePlayer);
    }

    private GameIsland(final Island island, final GamePlayer gamePlayer) {
        this.umbrella = new GameIslandUmbrella(this).getUmbrella();
        this.island = island;
        this.gamePlayer = gamePlayer;
    }

    public void start() {
        // setting the player's queue to true
        this.gamePlayer.startQueue();

        // binding the game player instance to bridge player
        this.gamePlayer.getBridgePlayer().setGamePlayer(gamePlayer);

        // reverse an island plot for this game
        this.islandPlot = SchematicManager.reservePlot(this);

        // execute the on join method on game island
        onJoin();

        // reset the player's queue
        this.gamePlayer.resetQueue();
    }

    public void onJoin() {
        final Player player = gamePlayer.getBridgePlayer().getPlayer();

        // setting the player island slot
        gamePlayer.setCurrentGame(this);
        // teleport the player to the island plot
        gamePlayer.teleport(islandPlot);

        // clears the player's inventory
        player.getInventory().clear();

        // active the game umbrella
        umbrella.activate(player);

        // add the block
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
            BridgeUtil.sendMessage(player, Message.INSTANCE.islandReset);
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

            umbrella.inactivate(player);

            // teleport the player to the lobby location
            final Location location = lobbyCategory.getLobbyLocation();
            if (location != null) {
                player.teleport(location);
            }
        }

        // remove the blocks
        gamePlayer.resetBlocks();

        // free the plot
        getIslandPlot().freePlot();

        // set the player's game to null, as they're leaving the island
        gamePlayer.setCurrentGame(null);
    }

    public CuboidRegion region() {
        final IslandPlot islandPlot = getIslandPlot();
        if (islandPlot == null) {
            return null;
        }
        return islandPlot.region();
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

    public void stopGame() {
        getIsland().leaveGame(getGamePlayer().getBridgePlayer());
    }
}
