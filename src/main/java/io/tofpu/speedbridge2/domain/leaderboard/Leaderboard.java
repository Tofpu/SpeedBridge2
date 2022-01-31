package io.tofpu.speedbridge2.domain.leaderboard;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Leaderboard {
    public static final Leaderboard INSTANCE = new Leaderboard();

    private final LoadingCache<UUID, BoardPlayer> playerCache;
    private final ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();

    private Leaderboard() {
        this.playerCache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                //                .refreshAfterWrite(5, TimeUnit.SECONDS)
                .build(BoardLoader.INSTANCE);
    }

    public void load() {
        executorService
                .scheduleWithFixedDelay(() -> {
                    System.out.println("refreshing!");
                    for (final UUID uuid : playerCache.asMap()
                            .keySet()) {
                        this.playerCache.refresh(uuid);
                    }
                }, 1, 10, TimeUnit.SECONDS);
    }

    public BoardPlayer retrieve(final UUID uniqueId) {
        return playerCache.getUnchecked(uniqueId);
    }

    public void shutdown() {
        executorService.shutdownNow();
    }
}
