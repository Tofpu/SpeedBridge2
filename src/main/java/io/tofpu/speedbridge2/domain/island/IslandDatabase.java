package io.tofpu.speedbridge2.domain.island;

import io.tofpu.speedbridge2.domain.common.database.wrapper.Database;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.DatabaseUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;

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
            databaseQuery.setInt(island.getSlot());
            databaseQuery.setString(island.getCategory());
            databaseQuery.setString(island.getSchematicName());
        });
    }

    public CompletableFuture<Void> update(final Island island) {
        return DatabaseUtil.databaseQueryExecute("UPDATE islands SET category = ?, schematicName = ? WHERE slot = ?", databaseQuery -> {
            BridgeUtil.debug("island category: " + island.getCategory());
            databaseQuery.setString(island.getCategory());

            BridgeUtil.debug("island schematic: " + island.getSchematicName());
            databaseQuery.setString(island.getSchematicName());

            databaseQuery.setInt(island.getSlot());
        });
    }

    public CompletableFuture<Void> delete(final int slot) {
        return DatabaseUtil.databaseQueryExecute("DELETE FROM islands WHERE slot = ?", databaseQuery -> {
            databaseQuery.setInt(slot);
        });
    }

    public CompletableFuture<List<Island>> getStoredIslands() {
        return runAsync(() -> {
            final List<Island> islands = new ArrayList<>();

            try {
                DatabaseUtil.databaseQuery("SELECT * FROM islands", resultSet -> {
                    while (resultSet.next()) {
                        final Island island = new Island(resultSet.getInt("slot"), resultSet.getString("category"));
                        island.selectSchematic(resultSet.getString("schematicName"));
                        islands.add(island);
                    }
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return islands;
        });
    }
}
