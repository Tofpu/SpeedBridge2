package io.tofpu.speedbridge2.domain.service;

import io.tofpu.speedbridge2.domain.GameIsland;
import io.tofpu.speedbridge2.domain.GamePlayer;
import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.handler.IslandHandler;
import io.tofpu.speedbridge2.domain.repository.IslandRepository;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;

public final class IslandService {
    public static final IslandService INSTANCE = new IslandService();

    private final IslandHandler islandHandler;
    private final IslandRepository islandRepository;
    public IslandService() {
        this.islandHandler = new IslandHandler();
        this.islandRepository = new IslandRepository();
    }

    public void load() {
        this.islandRepository.loadIslands().whenComplete((islandMap, throwable) -> {
            this.islandHandler.load(islandMap);
        });
    }

    public Island createIsland(final int slot) {
        return this.islandHandler.createIsland(slot);
    }

    public Island findIslandBy(final int slot) {
        return this.islandHandler.findIslandBy(slot);
    }

    public Map.Entry<GamePlayer, GameIsland> generateGame(final Player player, final Island island) {
        return island.generateGame(player);
    }

    public GameIsland findGameByPlayer(final GamePlayer gamePlayer, final Island island) {
        return island.findGameByPlayer(gamePlayer);
    }

    public GameIsland findGameByPlayer(final Player player, final Island island) {
        return findGameByPlayer(GamePlayer.of(player), island);
    }

    public Island deleteIsland(final int slot) {
        return this.islandHandler.deleteIsland(slot);
    }

    public Collection<Island> getAllIslands() {
        return this.islandHandler.getIslands();
    }
}
