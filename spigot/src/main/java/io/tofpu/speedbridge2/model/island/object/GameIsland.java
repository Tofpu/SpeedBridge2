package io.tofpu.speedbridge2.model.island.object;

import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.config.category.LobbyCategory;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.arena.ArenaManager;
import io.tofpu.speedbridge2.model.island.object.land.IslandLand;
import io.tofpu.speedbridge2.model.island.object.umbrella.GameIslandUmbrella;
import io.tofpu.speedbridge2.model.player.object.GamePlayer;
import io.tofpu.speedbridge2.model.player.object.stat.PlayerStatType;
import io.tofpu.speedbridge2.model.support.worldedit.CuboidRegion;
import io.tofpu.umbrella.domain.Umbrella;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GameIsland {
    private final ArenaManager arenaManager;
    private final Umbrella umbrella;
    private final Island island;
    private final GamePlayer gamePlayer;

    private IslandLand islandLand;

    public GameIsland(final ArenaManager arenaManager, final Island island,
                      final GamePlayer gamePlayer) {
        this.arenaManager = arenaManager;
        // todo: memory leak, as this does not get invalidated once done
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
        this.islandLand = arenaManager.reservePlot(this);

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
        gamePlayer.teleport(islandLand);

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
        gamePlayer.teleport(islandLand);

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
        getIslandPlot().free();

        // set the player's game to null, as they're leaving the island
        gamePlayer.setCurrentGame(null);
    }

    public CuboidRegion region() {
        final IslandLand islandLand = getIslandPlot();
        if (islandLand == null) {
            return null;
        }
        return islandLand.region();
    }

    public Island getIsland() {
        return island;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public IslandLand getIslandPlot() {
        return islandLand;
    }

    public boolean stopGame() {
        return getIsland().leaveGame(getGamePlayer().getBridgePlayer());
    }

    public void abortGame() {
        getIsland().abortGame(getGamePlayer().getBridgePlayer());
    }

    public void abortGame() {
        getIsland().abortGame(getGamePlayer().getBridgePlayer());
    }
}
