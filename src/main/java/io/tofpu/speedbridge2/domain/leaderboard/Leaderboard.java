package io.tofpu.speedbridge2.domain.leaderboard;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.leaderboard.loader.BoardLoader;
import io.tofpu.speedbridge2.domain.leaderboard.loader.IslandLoader;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.GlobalBoardPlayer;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.IslandBoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public final class Leaderboard {
    public static final Leaderboard INSTANCE = new Leaderboard();

    private final Map<Integer, GlobalBoardPlayer> globalMap;
    private final LoadingCache<UUID, GlobalBoardPlayer> playerCache;
    private final LoadingCache<UUID, IslandBoardPlayer> playerIslandCache;

    private final ScheduledExecutorService executorService;

    private Leaderboard() {
        this.globalMap = new ConcurrentHashMap<>();

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

            try (final DatabaseQuery databaseQuery = new DatabaseQuery(
                    "SELECT DISTINCT * FROM " + "scores ORDER BY score " + "LIMIT 10 OFFSET 0")) {
                final Map<Integer, GlobalBoardPlayer> globalBoardMap = new HashMap<>();

                try (final ResultSet resultSet = databaseQuery.executeQuery()) {
                    while (resultSet.next()) {
                        final int position = resultSet.getRow();
                        final UUID uuid = UUID.fromString(resultSet.getString("uid"));
                        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(uuid);

                        final GlobalBoardPlayer value = new GlobalBoardPlayer(position,
                                uuid, bridgePlayer);

                        globalBoardMap.put(position, value);
                    }
                }

                this.globalMap.clear();
                this.globalMap.putAll(globalBoardMap);
            } catch (Exception e) {
                e.printStackTrace();
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

    public CompletableFuture<GlobalBoardPlayer> retrieve(final int position) {
        final GlobalBoardPlayer boardPlayer = globalMap.get(position);
        System.out.println("position: " + position);

        return CompletableFuture.completedFuture(boardPlayer);
    }

    public CompletableFuture<GlobalBoardPlayer> retrieve(final int islandSlot, final int position) {
        final GlobalBoardPlayer boardPlayer = globalMap.get(position);
        System.out.println("position: " + position);

        return CompletableFuture.completedFuture(boardPlayer);
    }
}
