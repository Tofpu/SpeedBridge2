package io.tofpu.speedbridge2.domain.island;

import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import io.tofpu.speedbridge2.domain.island.object.Island;

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

    public IslandHandler.IslandCreationResult createIsland(final int slot, final String category,
            final String schematic) {
        return this.islandHandler.createIsland(slot, category, schematic);
    }

    public IslandHandler.IslandCreationResult createIsland(final int slot, final String category) {
        return this.islandHandler.createIsland(slot, category);
    }

    public Island findIslandBy(final int slot) {
        return this.islandHandler.findIslandBy(slot);
    }

    public Island findIslandBy(final String category) {
        return this.islandHandler.findIslandBy(category);
    }

    public Map.Entry<GamePlayer, GameIsland> generateGame(final BridgePlayer player,
            final Island island) {
        return island.generateGame(player);
    }

    public GameIsland findGameByPlayer(final BridgePlayer player, final Island island) {
        return findGameByPlayer(GamePlayer.of(player), island);
    }

    public GameIsland findGameByPlayer(final GamePlayer gamePlayer, final Island island) {
        return island.findGameByPlayer(gamePlayer);
    }

    public Island deleteIsland(final int slot) {
        return this.islandHandler.deleteIsland(slot);
    }

    public Collection<Island> getAllIslands() {
        return this.islandHandler.getIslands();
    }
}
