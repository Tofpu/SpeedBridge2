package io.tofpu.speedbridge2.model.player.object.stat;

import io.tofpu.speedbridge2.model.common.PluginExecutor;
import io.tofpu.speedbridge2.model.common.database.wrapper.Database;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.common.util.DatabaseUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.model.common.util.DatabaseUtil.runAsync;

public final class StatsDatabase extends Database {
    public StatsDatabase() {
        super(DatabaseTable.of("stats", "stats_id INTEGER PRIMARY KEY AUTOINCREMENT", "uid text NOT NULL", "key text NOT NULL", "value text NOT NULL"));
    }

    public @NotNull CompletableFuture<Void> insert(final @NotNull PlayerStat playerStat) {
        return DatabaseUtil.databaseExecute(
                "INSERT OR IGNORE INTO stats (uid, key, value) VALUES " +
                "(?, ?, ?)", databaseQuery -> {
                    databaseQuery.setString(playerStat.getOwner()
                            .toString());
                    databaseQuery.setString(playerStat.getKey());
                    databaseQuery.setString(playerStat.getValue());
                });
    }

    public @NotNull CompletableFuture<Void> update(final @NotNull PlayerStat playerStat) {
        return DatabaseUtil.databaseExecute("UPDATE stats SET value = ? WHERE " +
                                            "uid = ? AND key = ?", databaseQuery -> {
            databaseQuery.setString(playerStat.getValue());

            databaseQuery.setString(playerStat.getOwner()
                    .toString());
            databaseQuery.setString(playerStat.getKey());
        });
    }

    public @NotNull CompletableFuture<Collection<PlayerStat>> getStoredStats(final @NotNull UUID owner) {
        return runAsync(() -> {
            final List<PlayerStat> playerStats = new ArrayList<>();

            try {
                DatabaseUtil.databaseExecute("SELECT * FROM stats WHERE uid = ?", databaseQuery -> {
                    databaseQuery.setString(owner.toString());

                    databaseQuery.executeQuery(resultSet -> {
                        while (resultSet.next()) {
                            final PlayerStatType playerStatType = PlayerStatType.match(resultSet.getString("key"));

                            if (playerStatType == null) {
                                continue;
                            }
                            final PlayerStat playerStat = PlayerStatType.create(owner, playerStatType, resultSet.getString("value"));
                            BridgeUtil.debug("found stat: " + playerStat);

                            playerStats.add(playerStat);
                        }
                    });
                        })
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }

            return playerStats;
        });
    }

    public CompletableFuture<?> delete(final UUID uuid) {
        return PluginExecutor.runAsync(() -> {
            try (final DatabaseQuery query = DatabaseQuery.query(
                    "SELECT * FROM stats WHERE " + "uid = ?")) {
                query.setString(uuid.toString());
                query.execute();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }
}
