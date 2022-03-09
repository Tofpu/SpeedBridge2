package io.tofpu.speedbridge2.domain.island;

import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.GamePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class IslandService {
    public static final @NotNull IslandService INSTANCE = new IslandService();

    private final @NotNull IslandHandler islandHandler;
    private final @NotNull IslandRepository islandRepository;

    public IslandService() {
        this.islandHandler = new IslandHandler();
        this.islandRepository = new IslandRepository();
    }

    public CompletableFuture<Map<Integer, Island>> load() {
        return this.islandRepository.loadIslands().whenComplete((islandMap, throwable) -> {
            this.islandHandler.load(islandMap);
        });
    }

    public @NotNull IslandHandler.IslandCreationResult createIsland(final int slot,
            final @NotNull String category,
            final String schematic) {
        return this.islandHandler.createIsland(slot, category, schematic);
    }

    public @Nullable Island findIslandBy(final int slot) {
        return this.islandHandler.findIslandBy(slot);
    }

    public @Nullable Island findIslandBy(final @NotNull String category) {
        return this.islandHandler.findIslandBy(category);
    }

    public @Nullable GameIsland findGameByPlayer(final BridgePlayer player, final Island island) {
        return findGameByPlayer(GamePlayer.of(player), island);
    }

    public @Nullable GameIsland findGameByPlayer(final GamePlayer gamePlayer,
            final Island island) {
        return island.findGameByPlayer(gamePlayer);
    }

    public @Nullable Island deleteIsland(final int slot) {
        return this.islandHandler.deleteIsland(slot);
    }

    public @NotNull Collection<Island> getAllIslands() {
        return this.islandHandler.getIslands();
    }
}
