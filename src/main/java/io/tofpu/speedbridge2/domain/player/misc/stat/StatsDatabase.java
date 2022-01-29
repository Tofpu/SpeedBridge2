package io.tofpu.speedbridge2.domain.player.misc.stat;

import io.tofpu.speedbridge2.domain.common.database.wrapper.Database;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.DatabaseUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.domain.common.util.DatabaseUtil.runAsync;

public final class StatsDatabase extends Database {
    public StatsDatabase() {
        super(DatabaseTable.of("stats", "uid text PRIMARY KEY", "key text NOT NULL", "value text NOT NULL"));

        // TABLE: TOTAL_WINS
        // UID: AIAIA_AJAJA_AJAJ
        // VALUE: 13

        // TABLE: TOTAL_TRIES
        // UID: AIAIA_AJAJA_AJAJ
        // VALUE: 16
    }

    public CompletableFuture<Void> insert(final PlayerStat playerStat) {
        return DatabaseUtil.databaseQueryExecute(
                "INSERT OR IGNORE INTO stats VALUES " + "(?, ?, ?)", databaseQuery -> {
                    databaseQuery.setString(1, playerStat.getOwner()
                            .toString());
                    databaseQuery.setString(2, playerStat.getKey());
                    databaseQuery.setString(3, playerStat.getValue());
                });
    }

    public CompletableFuture<Void> update(final PlayerStat playerStat) {
        return DatabaseUtil.databaseQueryExecute(
                "UPDATE stats SET key = ?, value = ? " + "WHERE " +
                "uid = ?", databaseQuery -> {
                    databaseQuery.setString(1, playerStat.getKey());
                    databaseQuery.setString(2, playerStat.getValue());
                    databaseQuery.setString(3, playerStat.getOwner()
                            .toString());

                    System.out.println(playerStat);
                });
    }

    public CompletableFuture<Collection<PlayerStat>> getStoredStats(final UUID owner) {
        return runAsync(() -> {
            final List<PlayerStat> playerStats = new ArrayList<>();

            try {
                DatabaseUtil.databaseQueryExecute("SELECT * FROM stats WHERE uid = ?", databaseQuery -> {
                    databaseQuery.setString(1, owner.toString());

                    try (final ResultSet resultSet = databaseQuery.executeQuery()) {
                        while (resultSet.next()) {
                            final PlayerStatType playerStatType = PlayerStatType.match(resultSet.getString(2));

                            if (playerStatType == null) {
                                continue;
                            }
                            final PlayerStat playerStat = PlayerStatType.create(owner, playerStatType, resultSet.getString(3));
                            System.out.println("found stat: " + playerStat);

                            playerStats.add(playerStat);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return playerStats;
        });
    }
}
