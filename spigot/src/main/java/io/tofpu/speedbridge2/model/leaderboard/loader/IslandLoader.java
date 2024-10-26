package io.tofpu.speedbridge2.model.leaderboard.loader;

import com.github.benmanes.caffeine.cache.CacheLoader;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.leaderboard.object.IslandBoardPlayer;
import io.tofpu.speedbridge2.model.player.PlayerService;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class IslandLoader implements CacheLoader<UUID, IslandBoardPlayer>, BoardLoader<IslandBoardPlayer> {
    private final PlayerService playerService;

    public IslandLoader(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public @NotNull IslandBoardPlayer load(final @NotNull UUID key) throws Exception {
        BridgeUtil.debug("IslandLoader#load: attempting to load " + key);
        return retrieve(key);
    }

    @Override
    public @NonNull CompletableFuture<IslandBoardPlayer> asyncReload(final UUID key, final IslandBoardPlayer oldValue, final Executor executor) {
        return retrieveAsync(key, executor);
    }

    @Override
    public IslandBoardPlayer retrieve(final @NotNull UUID uniqueId) {
        return new IslandBoardPlayer(playerService, uniqueId);
    }

    @Override
    public @NotNull CompletableFuture<IslandBoardPlayer> retrieveAsync(final @NotNull UUID key,
                                                                       final @NotNull Executor executor) {
        BridgeUtil.debug("IslandLoader#retrieveAsync: retrieving " + key);
        return CompletableFuture.supplyAsync(() -> retrieve(key), executor);
    }
}
