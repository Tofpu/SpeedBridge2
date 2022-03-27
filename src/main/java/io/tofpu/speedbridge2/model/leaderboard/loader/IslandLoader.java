package io.tofpu.speedbridge2.model.leaderboard.loader;

import com.github.benmanes.caffeine.cache.CacheLoader;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.leaderboard.meta.BoardRetrieve;
import io.tofpu.speedbridge2.model.leaderboard.wrapper.IslandBoardPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class IslandLoader implements CacheLoader<UUID, IslandBoardPlayer>,
        BoardRetrieve<IslandBoardPlayer> {
    public static final IslandLoader INSTANCE = new IslandLoader();

    private IslandLoader() {}

    @Override
    public @NotNull IslandBoardPlayer load(final @NotNull UUID key) throws Exception {
        BridgeUtil.debug("attempting to load " + key);
        return retrieve(key);
    }

    @Override
    public CompletableFuture<? extends IslandBoardPlayer> asyncReload(final UUID key, final IslandBoardPlayer oldValue, final Executor executor) throws Exception {
        BridgeUtil.debug("attempting to reload " + key);
        return retrieveAsync(key, executor);
    }

    @Override
    public IslandBoardPlayer retrieve(final @NotNull UUID uniqueId) {
        return new IslandBoardPlayer(uniqueId);
    }

    @Override
    public @NotNull CompletableFuture<IslandBoardPlayer> retrieveAsync(final @NotNull UUID key,
            final @NotNull Executor executor) {
        BridgeUtil.debug("retrieving " + key);
        return CompletableFuture.supplyAsync(() -> retrieve(key), executor);
    }
}
