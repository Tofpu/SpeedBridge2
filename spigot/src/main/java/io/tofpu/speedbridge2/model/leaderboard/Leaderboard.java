package io.tofpu.speedbridge2.model.leaderboard;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.tofpu.speedbridge2.model.common.PluginExecutor;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.leaderboard.loader.IslandLoader;
import io.tofpu.speedbridge2.model.leaderboard.loader.PlayerPositionLoader;
import io.tofpu.speedbridge2.model.leaderboard.object.BoardPlayer;
import io.tofpu.speedbridge2.model.leaderboard.object.IslandBoardPlayer;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.score.Score;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class Leaderboard {
    private final PlayerService playerService;

    private final LeaderboardMap globalMap;
    private final Map<Integer, BoardPlayer> sessionMap;

    private final AsyncLoadingCache<UUID, BoardPlayer> positionMap;
    private final AsyncLoadingCache<UUID, IslandBoardPlayer> islandPositionMap;

    public Leaderboard(final PlayerService playerService) {
        this.playerService = playerService;
        this.globalMap = new LeaderboardMap();
        this.sessionMap = new ConcurrentHashMap<>();

        // player's personal global position
        this.positionMap = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .buildAsync(new PlayerPositionLoader(playerService));

        // player's global position that based on an island
        this.islandPositionMap = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .buildAsync(new IslandLoader(playerService));
    }

    /**
     * Load the leaderboard from the database
     *
     * @return Nothing.
     */
    public CompletableFuture<Void> loadAsync() {
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

                        final BoardPlayer value =
                                BridgeUtil.toBoardPlayer(true,
                                        resultSet);
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
            for (final UUID uuid : positionMap.asMap()
                    .keySet()) {
                this.positionMap.synchronous().refresh(uuid);
            }

            // update the global leaderboard
            globalMap.updateLeaderboard();
        }, 20L * ConfigurationManager.INSTANCE.getLeaderboardCategory()
                .getGlobalUpdateInterval(), 20L * ConfigurationManager.INSTANCE.getLeaderboardCategory()
                .getGlobalUpdateInterval());

        BridgeUtil.runBukkitAsync(() -> {
            // sessional leaderboard operation
            final Map<UUID, BoardPlayer> scoreMap = new HashMap<>();
            for (final BridgePlayer bridgePlayer : playerService.getBridgePlayers()) {
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
                    final UUID uuid = bridgePlayer.getPlayerUid();
                    scoreMap.put(uuid, new BoardPlayer(bridgePlayer.getName(), -1, uuid, bestScore));
                }
            }

            List<BoardPlayer> sortedPlayers = scoreMap.values().stream()
                    .sorted(Comparator.comparing(BoardPlayer::getScore))
                    .collect(Collectors.toList());

            this.sessionMap.clear();
            int position = 1;
            for (BoardPlayer boardPlayer : sortedPlayers) {
                this.sessionMap.put(position, new BoardPlayer(boardPlayer.getName(), position, boardPlayer.getOwner(), boardPlayer.getScore()));
                position++;
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
        final CompletableFuture<BoardPlayer> completableFuture = positionMap.asMap()
                .get(uniqueId);

        if (completableFuture == null) {
            // loading the board player
            return positionMap.get(uniqueId);
        }

        return completableFuture;
    }

    /**
     * Given a position, retrieve the player at that position from the leaderboard
     *
     * @param leaderboardRetrieveType The type of leaderboard to retrieve.
     * @param position                The position of the player in the leaderboard.
     * @return A BoardPlayer object.
     */
    public BoardPlayer retrieve(final LeaderboardRetrieveType leaderboardRetrieveType,
                                final int position) {
        switch (leaderboardRetrieveType) {
            case GLOBAL:
                return globalMap.get(position);
            case SESSION:
                return sessionMap.get(position);
            default:
                throw new IllegalStateException("Invalid LeaderboardRetrieveType: " + leaderboardRetrieveType.name());
        }
    }

    /**
     * Retrieve the island board for the given player and island slot.
     *
     * @param uniqueId   The UUID of the player.
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



