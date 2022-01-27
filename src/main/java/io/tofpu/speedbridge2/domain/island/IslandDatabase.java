package io.tofpu.speedbridge2.domain.island;

import io.tofpu.speedbridge2.domain.common.database.wrapper.Database;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.DatabaseUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.domain.common.util.DatabaseUtil.runAsync;

public class IslandDatabase extends Database {
    public IslandDatabase() {
        super(DatabaseTable.of("islands", "slot NOT NULL PRIMARY KEY", "category TEXT", "schematicName TEXT"));
    }

    public CompletableFuture<Void> insert(final Island island) {
        return DatabaseUtil.databaseQueryExecute("INSERT OR IGNORE INTO islands VALUES (?, ?, ?)", databaseQuery -> {
            databaseQuery.setInt(1, island.getSlot());
            databaseQuery.setString(2, island.getCategory());
            databaseQuery.setString(3, island.getSchematicName());
        });
    }

    public CompletableFuture<Void> update(final Island island) {
        return DatabaseUtil.databaseQueryExecute("UPDATE islands SET category = ?, schematicName = ? WHERE slot = ?", databaseQuery -> {
            System.out.println("island category: " + island.getCategory());
            databaseQuery.setString(1, island.getCategory());

            System.out.println("island schematic: " + island.getSchematicName());
            databaseQuery.setString(2, island.getSchematicName());

            databaseQuery.setInt(3, island.getSlot());
        });
    }

    public CompletableFuture<Void> delete(final int slot) {
        return DatabaseUtil.databaseQueryExecute("DELETE FROM islands WHERE slot = ?", databaseQuery -> {
            databaseQuery.setInt(1, slot);
        });
    }

    public CompletableFuture<List<Island>> getStoredIslands() {
        return runAsync(() -> {
            final List<Island> islands = new ArrayList<>();

            try {
                DatabaseUtil.databaseQuery("SELECT * FROM islands", resultSet -> {
                    try {
                        while (resultSet.next()) {
                            final Island island = new Island(resultSet.getInt(1), resultSet.getString(2));
                            island.selectSchematic(resultSet.getString(3));
                            islands.add(island);
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return islands;
        });
    }
}
