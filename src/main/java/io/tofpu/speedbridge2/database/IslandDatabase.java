package io.tofpu.speedbridge2.database;

import io.tofpu.speedbridge2.database.util.DatabaseUtil;
import io.tofpu.speedbridge2.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.Island;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.database.util.DatabaseUtil.runAsync;

public class IslandDatabase extends Database {
    IslandDatabase() {
        super(DatabaseTable.of("islands", "slot NOT NULL PRIMARY KEY", "category TEXT", "schematicName TEXT"));
    }

    public CompletableFuture<Void> insert(final Island island) {
        return DatabaseUtil.databaseQueryExecute("INSERT OR IGNORE INTO islands VALUES (?, ?, ?)", databaseQuery -> {
            databaseQuery.setInt(1, island.getSlot());
            databaseQuery.setString(2, island.getCategory());
            databaseQuery.setString(3, island.getIslandSchematic().getSchematicName());
        });
    }

    public CompletableFuture<Void> update(final Island island) {
        return DatabaseUtil.databaseQueryExecute("UPDATE islands SET category = ? WHERE slot = ?", databaseQuery -> {
            databaseQuery.setString(1, island.getCategory());
            System.out.println("island category: " + island.getCategory());
            databaseQuery.setInt(2, island.getSlot());
            System.out.println("island schematic: " + island.getIslandSchematic().getSchematicName());
            databaseQuery.setString(3, island.getIslandSchematic().getSchematicName());
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
                            islands.add(new Island(resultSet.getInt(1), resultSet.getString(2)));
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
