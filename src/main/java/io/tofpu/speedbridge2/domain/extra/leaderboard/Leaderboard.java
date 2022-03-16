package io.tofpu.speedbridge2.domain.extra.leaderboard;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.extra.leaderboard.loader.IslandLoader;
import io.tofpu.speedbridge2.domain.extra.leaderboard.loader.PersonalBoardLoader;
import io.tofpu.speedbridge2.domain.extra.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.extra.leaderboard.wrapper.IslandBoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class Leaderboard {
    public static final Leaderboard INSTANCE = new Leaderboard();

    private final LeaderboardMap globalMap;
    private final Map<Integer, BoardPlayer> sessionalMap;

    private final AsyncLoadingCache<UUID, BoardPlayer> playerCache;
    private final AsyncLoadingCache<UUID, IslandBoardPlayer> islandPositionMap;

    private Leaderboard() {
        this.globalMap = new LeaderboardMap();
        this.sessionalMap = new ConcurrentHashMap<>();

        // player's personal global position
        this.playerCache = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .buildAsync(PersonalBoardLoader.INSTANCE);

        // player's global position that based on an island
        this.islandPositionMap = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .buildAsync(IslandLoader.INSTANCE);
    }

    /**
     * Load the leaderboard from the database
     *
     * @return Nothing.
     */
    public CompletableFuture<Void> load() {
        final CompletableFuture<Void> loadFuture = new CompletableFuture<>();

        BridgeUtil.runBukkitAsync(() -> {
            BridgeUtil.debug("Leaderboard#load(): loading the leaderboard!");

            // global leaderboard operation
            try (final DatabaseQuery databaseQuery = DatabaseQuery.query("SELECT DISTINCT *" +
                                                                     " FROM scores ORDER BY score")) {
                final List<UUID> uuidList = new ArrayList<>();
                final Map<Integer, BoardPlayer> globalBoardMap = new HashMap<>();

                databaseQuery.executeQuery(resultSet -> {
                    while (resultSet.next()) {
                        // if we reached the 10 limit, break the loop
                        if (globalBoardMap.size() == 10) {
                            break;
                        }

                        final String uid = resultSet.getString("uid");
                        if (uid == null) {
                            continue;
                        }

                        final UUID uuid = UUID.fromString(uid);
                        BridgeUtil.debug("Leaderboard#load(): uuid == " + uuid);
                        // if we already have the given uuid, continue through the loop!
                        if (uuidList.contains(uuid)) {
                            BridgeUtil.debug("Leaderboard#load(): uuidList contains " + uuid + "; continuing");
                            continue;
                        }

                        final BoardPlayer value = BridgeUtil.toBoardPlayer(true, resultSet);
                        BridgeUtil.debug("Leaderboard#load(): value == " + value);
                        if (value == null) {
                            BridgeUtil.debug("Leaderboard#load(): value == null; " +
                                             "continuing");
                            continue;
                        }

                        uuidList.add(uuid);
                        globalBoardMap.put(value.getPosition(), value);
                    }
                });

                this.globalMap.load(globalBoardMap);
                loadFuture.complete(null);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });

        BridgeUtil.runBukkitAsync(() -> {
                    BridgeUtil.debug("Leaderboard#load(): refreshing the leaderboard!");

                    // per-player based position operation
                    for (final UUID uuid : playerCache.asMap()
                            .keySet()) {
                        this.playerCache.synchronous().refresh(uuid);
                    }

                    // update the global leaderboard
                    globalMap.updateLeaderboard();
                }, 20L * ConfigurationManager.INSTANCE.getLeaderboardCategory()
                        .getGlobalUpdateInterval(), 20L * ConfigurationManager.INSTANCE.getLeaderboardCategory()
                        .getGlobalUpdateInterval());

        BridgeUtil.runBukkitAsync(() -> {
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
                }, ConfigurationManager.INSTANCE.getLeaderboardCategory()
                        .getSessionUpdateInterval(), 20L * ConfigurationManager.INSTANCE.getLeaderboardCategory()
                        .getSessionUpdateInterval());

        return loadFuture;
    }

    /**
     * Retrieve a player board from the cache, or load it from the database if it's not in
     * the cache.
     *
     * @param uniqueId The unique ID of the player.
     * @return A CompletableFuture<BoardPlayer>
     */
    public CompletableFuture<BoardPlayer> retrieve(final UUID uniqueId) {
        final CompletableFuture<BoardPlayer> completableFuture = playerCache.asMap()
                .get(uniqueId);

        if (completableFuture == null) {
            // loading the board player
            return playerCache.get(uniqueId);
        }

        return completableFuture;
    }

    /**
     * Given a position, retrieve the player at that position from the leaderboard
     *
     * @param leaderboardRetrieveType The type of leaderboard to retrieve.
     * @param position The position of the player in the leaderboard.
     * @return A BoardPlayer object.
     */
    public BoardPlayer retrieve(final LeaderboardRetrieveType leaderboardRetrieveType,
            final int position) {
        switch (leaderboardRetrieveType) {
            case GLOBAL:
                return globalMap.get(position);
            case SESSION:
                return sessionalMap.get(position);
            default:
                throw new IllegalStateException("Invalid LeaderboardRetrieveType: " + leaderboardRetrieveType.name());
        }
    }

    /**
     * Retrieve the island board for the given player and island slot.
     *
     * @param uniqueId The UUID of the player.
     * @param islandSlot The slot of the island board to retrieve.
     * @return The IslandBoard object.
     */
    public CompletableFuture<IslandBoardPlayer.IslandBoard> retrieve(final UUID uniqueId, final int islandSlot) {
        final IslandBoardPlayer player =
                islandPositionMap.synchronous().getIfPresent(uniqueId);
        final IslandBoardPlayer.IslandBoard islandBoard =
                player == null ? null : player.findDefault(islandSlot);

        // if an island board is found, return the completed value
        if (islandBoard != null) {
            return CompletableFuture.completedFuture(islandBoard);
        }

        // otherwise, attempt to retrieve the board async
        return PluginExecutor.supply(() -> {
            try {
                return islandPositionMap.get(uniqueId).get().retrieve(islandSlot);
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    /**
     * Add a score to the global map
     *
     * @param owner The player who owns the score.
     * @param score The score to add to the global map.
     */
    public void addScore(final BridgePlayer owner, final Score score) {
        this.globalMap.append(owner, score);
    }

    /**
     * This function resets the global map for the player with the given playerUid
     *
     * @param playerUid The UUID of the player who's map is being reset.
     */
    public void reset(final UUID playerUid) {
        this.globalMap.reset(playerUid);
    }

    public enum LeaderboardRetrieveType {
        GLOBAL, SESSION;
    }
}



