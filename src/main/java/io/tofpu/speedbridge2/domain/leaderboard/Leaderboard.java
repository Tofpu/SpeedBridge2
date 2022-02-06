package io.tofpu.speedbridge2.domain.leaderboard;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.leaderboard.loader.IslandLoader;
import io.tofpu.speedbridge2.domain.leaderboard.loader.PersonalBoardLoader;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.IslandBoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class Leaderboard {
    public static final Leaderboard INSTANCE = new Leaderboard();

    private final Map<Integer, BoardPlayer> globalMap;
    private final Map<Integer, BoardPlayer> sessionalMap;

    private final LoadingCache<UUID, BoardPlayer> playerCache;
    private final LoadingCache<UUID, IslandBoardPlayer> islandPositionMap;

    private Leaderboard() {
        this.globalMap = new ConcurrentHashMap<>();
        this.sessionalMap = new ConcurrentHashMap<>();

        // player's personal global position
        this.playerCache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(PersonalBoardLoader.INSTANCE);

        // player's global position that based on an island
        this.islandPositionMap = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(IslandLoader.INSTANCE);
    }

    public void load(final JavaPlugin javaPlugin) {
        Bukkit.getScheduler()
                .runTaskTimerAsynchronously(javaPlugin, () -> {
                    BridgeUtil.debug("refreshing the leaderboard!");

                    // per-player based position operation
                    for (final UUID uuid : playerCache.asMap()
                            .keySet()) {
                        this.playerCache.refresh(uuid);
                    }

                    // sessional leaderboard operation
                    final Map<UUID, BoardPlayer> scoreMap = new HashMap<>();
                    for (final BridgePlayer bridgePlayer : PlayerService.INSTANCE.getBridgePlayers()) {
                        if (scoreMap.size() == 10) {
                            break;
                        }

                        Score bestScore = null;
                        for (final Score score : bridgePlayer.getSessionScores()) {
                            // if the best score is not null, and best score is higher
                            // than or equal to 0
                            if (bestScore != null && bestScore.compareTo(score) >= 0) {
                                continue;
                            }
                            bestScore = score;
                        }

                        if (bestScore != null) {
                            final int position = scoreMap.size() + 1;
                            final UUID uuid = bridgePlayer.getPlayerUid();
                            scoreMap.put(uuid, new BoardPlayer(bridgePlayer.getName(), position, uuid, bestScore));
                        }
                    }

                    this.sessionalMap.clear();
                    for (final Map.Entry<UUID, BoardPlayer> entry : scoreMap.entrySet()) {
                        final BoardPlayer value = entry.getValue();
                        this.sessionalMap.put(value.getPosition(), value);
                    }

                    // global leaderboard operation
                    try (final DatabaseQuery databaseQuery = new DatabaseQuery("SELECT DISTINCT * FROM scores ORDER BY score")) {
                        final List<UUID> uuidList = new ArrayList<>();
                        final Map<Integer, BoardPlayer> globalBoardMap = new HashMap<>();

                        databaseQuery.executeQuery(resultSet -> {
                            while (resultSet.next()) {
                                // if we reached the 10 limit, break the loop
                                if (globalBoardMap.size() == 10) {
                                    break;
                                }

                                final UUID uuid = UUID.fromString(resultSet.getString("uid"));
                                // if we already have the given uuid, continue through the loop!
                                if (uuidList.contains(uuid)) {
                                    continue;
                                }

                                final BoardPlayer value = BridgeUtil.resultToBoardPlayer(true, resultSet);

                                uuidList.add(uuid);
                                globalBoardMap.put(value.getPosition(), value);
                            }
                        });

                        this.globalMap.clear();
                        this.globalMap.putAll(globalBoardMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, 1, 20L * ConfigurationManager.INSTANCE.getLeaderboardCategory()
                        .getUpdateInterval());
    }

    public CompletableFuture<BoardPlayer> retrieve(final UUID uniqueId) {
        final BoardPlayer player = playerCache.asMap()
                .get(uniqueId);

        // if the board player is found, return the completed value
        if (player != null) {
            return CompletableFuture.completedFuture(player);
        }

        // otherwise, attempt to load the player board async
        return CompletableFuture.supplyAsync(() -> playerCache.getUnchecked(uniqueId));
    }

    public BoardPlayer retrieve(final LeaderboardRetrieveType leaderboardRetrieveType,
            final int position) {
        switch (leaderboardRetrieveType) {
            case GLOBAL:
                return globalMap.get(position);
            case SESSION:
                return sessionalMap.get(position);
        }
        return null;
    }

    public CompletableFuture<IslandBoardPlayer.IslandBoard> retrieve(final UUID uniqueId, final int islandSlot) {
        final IslandBoardPlayer player = islandPositionMap.asMap()
                .get(uniqueId);
        final IslandBoardPlayer.IslandBoard islandBoard =
                player == null ? null : player.findDefault(islandSlot);

        // if an island board is found, return the completed value
        if (islandBoard != null) {
            return CompletableFuture.completedFuture(islandBoard);
        }

        // otherwise, attempt to retrieve the board async
        return PluginExecutor.supply(() -> islandPositionMap.getUnchecked(uniqueId)
                .retrieve(islandSlot));
    }

    public enum LeaderboardRetrieveType {
        GLOBAL, SESSION;
    }
}



