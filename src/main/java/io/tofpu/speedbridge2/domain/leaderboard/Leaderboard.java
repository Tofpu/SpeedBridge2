package io.tofpu.speedbridge2.domain.leaderboard;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.IslandPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Leaderboard {
    public static final Leaderboard INSTANCE = new Leaderboard();

    private final LoadingCache<UUID, BoardPlayer> playerCache;
    private final LoadingCache<UUID, IslandPlayer> playerIslandCache;
    private final ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();

    private Leaderboard() {
        this.playerCache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(BoardLoader.INSTANCE);
        this.playerIslandCache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(IslandLoader.INSTANCE);
    }

    public void load() {
        executorService.scheduleWithFixedDelay(() -> {
            System.out.println("refreshing!");
            for (final UUID uuid : playerCache.asMap()
                    .keySet()) {
                this.playerCache.refresh(uuid);
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    public CompletableFuture<BoardPlayer> retrieve(final UUID uniqueId) {
        final BoardPlayer player = playerCache.asMap()
                .get(uniqueId);

        if (player != null) {
            return CompletableFuture.completedFuture(player);
        }

        return CompletableFuture.supplyAsync(() -> playerCache.getUnchecked(uniqueId));
    }

    public CompletableFuture<IslandPlayer.IslandBoard> retrieve(final UUID uniqueId, final int islandSlot) {
        final IslandPlayer player = playerIslandCache.asMap()
                .get(uniqueId);

        if (player != null) {
            final CompletableFuture<IslandPlayer.IslandBoard> islandBoard = player.retrieve(islandSlot);

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
