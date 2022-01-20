package io.tofpu.speedbridge2.database;

import io.tofpu.speedbridge2.database.manager.DatabaseManager;
import io.tofpu.speedbridge2.database.util.DatabaseUtil;
import io.tofpu.speedbridge2.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.BridgePlayer;
import io.tofpu.speedbridge2.domain.misc.Score;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.database.util.DatabaseUtil.runAsync;

public final class PlayerDatabase extends Database {
    public PlayerDatabase() {
        super(DatabaseTable.of("players", "uid text PRIMARY KEY"));
        DatabaseManager.appendTable(DatabaseTable.of("scores", "uid text PRIMARY KEY", "islandSlot int NOT NULL", "score int NOT NULL"));
    }

    public CompletableFuture<Void> insert(final BridgePlayer player) {
        return DatabaseUtil.databaseQueryExecute("INSERT OR IGNORE INTO players VALUES (?)", databaseQuery -> {
            databaseQuery.setString(1, player.getPlayerUid().toString());
        });
    }

    public CompletableFuture<Void> insert(final UUID uuid, final Score score) {
        return DatabaseUtil.databaseQueryExecute("INSERT OR IGNORE INTO scores VALUES " +
                "(?, ?, ?)", databaseQuery -> {
            System.out.println("player uid: " + uuid.toString());
            databaseQuery.setString(1, uuid.toString());

            System.out.println("player score island: " + score.getScoredOn());
            databaseQuery.setInt(2, score.getScoredOn());

            System.out.println("player score: " + score.getScore());
            databaseQuery.setDouble(3, score.getScore());

        });
    }

    public CompletableFuture<Void> update(final UUID uuid, final Score score) {
        return DatabaseUtil.databaseQueryExecute("UPDATE scores SET islandSlot = ?, score = ? WHERE uid = ?", databaseQuery -> {
            System.out.println("player uid: " + uuid.toString());

            System.out.println("player score island: " + score.getScoredOn());
            databaseQuery.setInt(1, score.getScoredOn());

            System.out.println("player score: " + score.getScore());
            databaseQuery.setDouble(2, score.getScore());

            databaseQuery.setString(3, uuid.toString());
        });
    }

    public CompletableFuture<Void> update(final BridgePlayer player) {
        final List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        System.out.println("player uid: " + player.getPlayerUid());

        for (final Score score : player.getScores()) {
            final CompletableFuture<Void> future = DatabaseUtil.databaseQueryExecute("UPDATE scores SET islandSlot = ?, " + "score = ? WHERE uid = ?", databaseQuery -> {
                System.out.println("player score island: " + score.getScoredOn());
                databaseQuery.setInt(1, score.getScoredOn());

                System.out.println("player score: " + score.getScore());
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
                System.out.println("attempting to load all the players");
                DatabaseUtil.databaseQuery("SELECT * FROM players", resultSet -> {
                    try {
                        while (resultSet.next()) {
                            final BridgePlayer bridgePlayer = BridgePlayer.of(UUID.fromString(resultSet
                                    .getString(1)));

                            System.out.println("found another player! " + bridgePlayer.getPlayerUid());

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
                    DatabaseUtil.runAsync(() -> {
                        try (final DatabaseQuery query = new DatabaseQuery("SELECT * FROM scores WHERE uid = ?")) {
                            query.setString(1, bridgePlayer.getPlayerUid()
                                    .toString());

                            try (final ResultSet set = query.executeQuery()) {
                                while (set.next()) {
                                    final Score score = Score.of(set.getInt(2), set
                                            .getLong(3));
                                    System.out.println("found new score! " + score);
                                    bridgePlayer.setInternalNewScore(score);
                                }
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            return bridgePlayers;
        });
    }
}
