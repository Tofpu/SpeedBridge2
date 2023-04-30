package io.tofpu.speedbridge2.game;

import io.tofpu.speedbridge2.game.arena.LandArea;
import io.tofpu.speedbridge2.game.umbrella.GameUmbrella;
import io.tofpu.speedbridge2.model.common.Message;
import io.tofpu.speedbridge2.model.common.config.category.LobbyCategory;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.object.stat.PlayerStatType;
import io.tofpu.umbrella.domain.Umbrella;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class DefaultGameStrategy extends GameStrategy {
    private Umbrella umbrella;
    private GamePlayer player;
    private LandArea landArea;

    protected DefaultGameStrategy(GameFactory gameFactory) {
        super(gameFactory);
    }

    @Override
    public void start() {
        this.umbrella = new GameUmbrella(gameFactory.getGameSession()).getUmbrella();
        this.player = gameFactory.getPlayer();
        this.landArea = gameFactory.getLandArea();

        player.startQueue();

        setupPlayer(player, gameFactory);

        player.resetQueue();
    }

    @Override
    public void reset(final boolean notify) {
        player.resetBlocks();
        player.resetTimer();
        player.teleport(landArea);

        player.increment(PlayerStatType.TOTAL_TRIES);

        player.getInventory().setItem(0,
                new ItemStack(player.getChoseMaterial(),
                        64));

        if (notify) {
            BridgeUtil.sendMessage(player.getSpigotPlayer(), Message.INSTANCE.islandReset);
        }
    }

    @Override
    public void stop() {
        this.player.getInventory().clear();
        umbrella.inactivate(player.getSpigotPlayer());

        final LobbyCategory lobbyCategory = ConfigurationManager.INSTANCE.getLobbyCategory();

        // teleport the player to the lobby location
        final Location location = lobbyCategory.getLobbyLocation();
        if (location != null) {
            player.teleport(location);
        }

        // remove the blocks
        player.resetBlocks();

        // free the plot
        landArea.setReserved(false);

        // set the player's game to null, as they're leaving the island
        player.setCurrentGame(null);
    }

    private void setupPlayer(GamePlayer gamePlayer, GameFactory gameFactory) {
        player.setCurrentGame(gameFactory.getGameSession());

        // teleport the player to the island plot
        gamePlayer.teleport(gameFactory.getLandArea());

        // clears the player's inventory
        player.getInventory().clear();

        // active the game umbrella
        umbrella.activate(player.getSpigotPlayer());

        // add the block
        player.getInventory()
                .setItem(0, new ItemStack(gamePlayer.getChoseMaterial(), 64));

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        player.setGameMode(GameMode.SURVIVAL);
    }
}