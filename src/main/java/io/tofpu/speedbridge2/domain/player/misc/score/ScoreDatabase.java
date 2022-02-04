package io.tofpu.speedbridge2.domain.player.misc.score;

import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.database.wrapper.Database;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class ScoreDatabase extends Database {
    private static final String[] SCORE_COLUMNS = {"id INTEGER PRIMARY KEY AUTOINCREMENT", "uid text NOT NULL", "island_slot INTEGER NOT NULL", "score REAL NOT NULL"};

    public ScoreDatabase() {
        super(DatabaseTable.of("scores", SCORE_COLUMNS));
    }

    public CompletableFuture<Void> insert(final UUID uuid, final Score score) {
        return DatabaseUtil.databaseQueryExecute(
                "INSERT OR IGNORE INTO scores (uid, island_slot, score) VALUES " +
                "(?, ?, ?)", databaseQuery -> {
                    BridgeUtil.debug("player uid: " + uuid.toString());
                    databaseQuery.setString(uuid.toString());

                    BridgeUtil.debug("player score island: " + score.getScoredOn());
                    databaseQuery.setInt(score.getScoredOn());

                    BridgeUtil.debug("player score: " + score.getScore());
                    databaseQuery.setDouble(score.getScore());

                });
    }

    public CompletableFuture<Void> update(final UUID uuid, final Score score) {
        return DatabaseUtil.databaseQueryExecute("UPDATE scores SET island_slot = ?, " +
                                                 "score = ? WHERE uid = ?", databaseQuery -> {
            BridgeUtil.debug("player uid: " + uuid.toString());

            BridgeUtil.debug("player score island: " + score.getScoredOn());
            databaseQuery.setInt(score.getScoredOn());

            BridgeUtil.debug("player score: " + score.getScore());
            databaseQuery.setDouble(score.getScore());

            databaseQuery.setString(uuid.toString());
        });
    }

    public CompletableFuture<List<Score>> getStoredScore(final UUID uniqueId) {
        return PluginExecutor.supply(() -> {
            final List<Score> scores = new ArrayList<>();

            try (final DatabaseQuery query = new DatabaseQuery("SELECT * FROM scores WHERE uid = ?")) {
                query.setString(uniqueId.toString());

                query.executeQuery(resultSet -> {
                    while (resultSet.next()) {
                        final Score score = Score.of(resultSet.getInt("island_slot"), resultSet.getDouble("score"));
                        BridgeUtil.debug("found new score! " + score);
                        scores.add(score);
                    }
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return scores;
        });
    }
}
