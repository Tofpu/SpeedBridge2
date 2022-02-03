package io.tofpu.speedbridge2.domain.player;

import io.tofpu.speedbridge2.domain.common.database.DatabaseManager;
import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.common.database.wrapper.Database;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.DatabaseUtil;
import io.tofpu.speedbridge2.domain.player.misc.Score;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStat;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.domain.common.util.DatabaseUtil.runAsync;

public final class PlayerDatabase extends Database {
    private static final String[] SCORE_COLUMNS = {"id INTEGER PRIMARY KEY AUTOINCREMENT", "uid text NOT NULL", "island_slot INTEGER NOT NULL", "score REAL NOT NULL"};

    public PlayerDatabase() {
        super(DatabaseTable.of("players", "uid text PRIMARY KEY"));
        DatabaseManager.appendTable(DatabaseTable.of("scores", SCORE_COLUMNS));
    }

    public CompletableFuture<Void> insert(final BridgePlayer player) {
        return DatabaseUtil.databaseQueryExecute("INSERT OR IGNORE INTO players VALUES (?)", databaseQuery -> {
            databaseQuery.setString(1, player.getPlayerUid()
                    .toString());
        });
    }

    public CompletableFuture<Void> insert(final UUID uuid, final Score score) {
        return DatabaseUtil.databaseQueryExecute(
                "INSERT OR IGNORE INTO scores (uid, island_slot, score) VALUES " +
                "(?, ?, ?)", databaseQuery -> {
                    BridgeUtil.debug("player uid: " + uuid.toString());
                    databaseQuery.setString(1, uuid.toString());

                    BridgeUtil.debug("player score island: " + score.getScoredOn());
                    databaseQuery.setInt(2, score.getScoredOn());

                    BridgeUtil.debug("player score: " + score.getScore());
                    databaseQuery.setDouble(3, score.getScore());

                });
    }

    public CompletableFuture<Void> update(final UUID uuid, final Score score) {
        return DatabaseUtil.databaseQueryExecute("UPDATE scores SET island_slot = ?, " +
                                                 "score = ? WHERE uid = ?", databaseQuery -> {
            BridgeUtil.debug("player uid: " + uuid.toString());

            BridgeUtil.debug("player score island: " + score.getScoredOn());
            databaseQuery.setInt(1, score.getScoredOn());

            BridgeUtil.debug("player score: " + score.getScore());
            databaseQuery.setDouble(2, score.getScore());

            databaseQuery.setString(3, uuid.toString());
        });
    }

    public CompletableFuture<Void> update(final BridgePlayer player) {
        final List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        BridgeUtil.debug("player uid: " + player.getPlayerUid());

        for (final Score score : player.getScores()) {
            final CompletableFuture<Void> future = DatabaseUtil.databaseQueryExecute("UPDATE scores SET islandSlot = ?, " + "score = ? WHERE uid = ?", databaseQuery -> {
                BridgeUtil.debug("player score island: " + score.getScoredOn());
                databaseQuery.setInt(1, score.getScoredOn());

                BridgeUtil.debug("player score: " + score.getScore());
                databaseQuery.setDouble(2, score.getScore());

                databaseQuery.setString(3, player.getPlayerUid().toString());
            });
            completableFutures.add(future);
        }
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
    }

    public CompletableFuture<Void> delete(final UUID uuid) {
        return DatabaseUtil.databaseQueryExecute("DELETE FROM players WHERE uid = ?", databaseQuery -> {
            databaseQuery.setString(1, uuid.toString());
        });
    }

    public CompletableFuture<List<BridgePlayer>> getStoredPlayers() {
        return runAsync(() -> {
            final List<BridgePlayer> bridgePlayers = new ArrayList<>();

            try {
                BridgeUtil.debug("attempting to load all the players");
                DatabaseUtil.databaseQuery("SELECT * FROM players", resultSet -> {
                    try {
                        while (resultSet.next()) {
                            final BridgePlayer bridgePlayer = BridgePlayer.of(UUID.fromString(resultSet
                                    .getString(1)));

                            BridgeUtil.debug("found another player! " + bridgePlayer.getPlayerUid());

                            bridgePlayers.add(bridgePlayer);
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            for (final BridgePlayer bridgePlayer : bridgePlayers) {
                try {
                    runAsync(() -> {
                        try (final DatabaseQuery query = new DatabaseQuery("SELECT * FROM scores WHERE uid = ?")) {
                            query.setString(1, bridgePlayer.getPlayerUid()
                                    .toString());

                            try (final ResultSet set = query.executeQuery()) {
                                while (set.next()) {
                                    final Score score = Score.of(set.getInt(3), set.getDouble(4));
                                    BridgeUtil.debug("found new score! " + score);
                                    bridgePlayer.setInternalNewScore(score);
                                }
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }).get();

                    final Collection<PlayerStat> playerStats = Databases.STATS_DATABASE.getStoredStats(bridgePlayer.getPlayerUid())
                            .get();

                    for (final PlayerStat playerStat : playerStats) {
                        bridgePlayer.setInternalStat(playerStat);
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            BridgeUtil.debug("players result: " + bridgePlayers);

            return bridgePlayers;
        });
    }
}
