package io.tofpu.speedbridge2.domain.service;

import io.tofpu.speedbridge2.domain.GameIsland;
import io.tofpu.speedbridge2.domain.GamePlayer;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.handler.IslandHandler;
import org.bukkit.entity.Player;

import java.util.Map;

public final class IslandService {
    public static final IslandService INSTANCE = new IslandService();

    private final IslandHandler islandHandler;
    public IslandService() {
        this.islandHandler = new IslandHandler();
    }

    public Island createIsland(final int slot) {
        return this.islandHandler.createIsland(slot);
    }

    public Map.Entry<GamePlayer, GameIsland> generateGame(final Player player, final Island island) {
        return island.generateGame(player);
    }

    public GameIsland findGameByPlayer(final GamePlayer gamePlayer, final Island island) {
        return island.findGameByPlayer(gamePlayer);
    }

    public GameIsland findGameByPlayer(final Player player, final Island island) {
        return findGameByPlayer(new GamePlayer(player), island);
    }
}
