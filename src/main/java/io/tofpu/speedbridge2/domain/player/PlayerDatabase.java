package io.tofpu.speedbridge2.domain.player;

import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.common.database.wrapper.Database;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.DatabaseUtil;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStat;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.domain.common.util.DatabaseUtil.runAsync;

public final class PlayerDatabase extends Database {
    public PlayerDatabase() {
        super(DatabaseTable.of("players", "uid text PRIMARY KEY"));
    }

    public CompletableFuture<Void> insert(final BridgePlayer player) {
        return DatabaseUtil.databaseQueryExecute("INSERT OR IGNORE INTO players VALUES (?)", databaseQuery -> {
            databaseQuery.setString(player.getPlayerUid()
                    .toString());
        });
    }

    public CompletableFuture<Void> update(final BridgePlayer player) {
        final List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        BridgeUtil.debug("player uid: " + player.getPlayerUid());

        for (final Score score : player.getScores()) {
            final CompletableFuture<Void> future = DatabaseUtil.databaseQueryExecute("UPDATE scores SET islandSlot = ?, " + "score = ? WHERE uid = ?", databaseQuery -> {
                BridgeUtil.debug("player score island: " + score.getScoredOn());
                databaseQuery.setInt(score.getScoredOn());

                BridgeUtil.debug("player score: " + score.getScore());
                databaseQuery.setDouble(score.getScore());

                databaseQuery.setString(player.getPlayerUid().toString());
            });
            completableFutures.add(future);
        }
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
    }

    public CompletableFuture<Void> delete(final UUID uuid) {
        return DatabaseUtil.databaseQueryExecute("DELETE FROM players WHERE uid = ?", databaseQuery -> {
            databaseQuery.setString(uuid.toString());
        });
    }

    public CompletableFuture<BridgePlayer> getStoredPlayer(final UUID uniqueId) {
        return runAsync(() -> {
            final BridgePlayer bridgePlayer = BridgePlayer.of(uniqueId);

            try (final DatabaseQuery query = new DatabaseQuery(
                    "SELECT * FROM players " + "where uid = ?")) {
                query.setString(uniqueId.toString());

                // if the execution returns false, that means the player is new
                if (!query.execute()) {
                    insert(bridgePlayer);
                    return bridgePlayer;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                final Collection<Score> scoreList = Databases.SCORE_DATABASE.getStoredScore(uniqueId)
                        .get();

                final Collection<PlayerStat> playerStats = Databases.STATS_DATABASE.getStoredStats(bridgePlayer.getPlayerUid())
                        .get();

                for (final Score score : scoreList) {
                    bridgePlayer.setInternalNewScore(score);
                }

                for (final PlayerStat playerStat : playerStats) {
                    bridgePlayer.setInternalStat(playerStat);
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            BridgeUtil.debug("successfully loaded " + uniqueId + " player's data!");

            return bridgePlayer;
        });
    }
}
