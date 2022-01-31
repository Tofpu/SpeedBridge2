package io.tofpu.speedbridge2.domain.leaderboard;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.leaderboard.loader.BoardLoader;
import io.tofpu.speedbridge2.domain.leaderboard.loader.IslandLoader;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.GlobalBoardPlayer;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.IslandBoardPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Leaderboard {
    public static final Leaderboard INSTANCE = new Leaderboard();

    private final LoadingCache<UUID, GlobalBoardPlayer> playerCache;
    private final LoadingCache<UUID, IslandBoardPlayer> playerIslandCache;

    private final ScheduledExecutorService executorService;

    private Leaderboard() {
        this.playerCache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(BoardLoader.INSTANCE);

        this.playerIslandCache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(IslandLoader.INSTANCE);

        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void load() {
        executorService.scheduleWithFixedDelay(() -> {
            BridgeUtil.debug("refreshing!");
            for (final UUID uuid : playerCache.asMap()
                    .keySet()) {
                this.playerCache.refresh(uuid);
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    public CompletableFuture<GlobalBoardPlayer> retrieve(final UUID uniqueId) {
        final GlobalBoardPlayer player = playerCache.asMap()
                .get(uniqueId);

        if (player != null) {
            return CompletableFuture.completedFuture(player);
        }

        return CompletableFuture.supplyAsync(() -> playerCache.getUnchecked(uniqueId));
    }

    public CompletableFuture<IslandBoardPlayer.IslandBoard> retrieve(final UUID uniqueId, final int islandSlot) {
        final IslandBoardPlayer player = playerIslandCache.asMap()
                .get(uniqueId);

        if (player != null) {
            final CompletableFuture<IslandBoardPlayer.IslandBoard> islandBoard = player.retrieve(islandSlot);

            if (islandBoard.isDone()) {
                return islandBoard;
            }
        }

        return playerIslandCache.getUnchecked(uniqueId)
                .retrieve(islandSlot);
    }

    public void shutdown() {
        executorService.shutdownNow();
    }
}
