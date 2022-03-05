package io.tofpu.speedbridge2.domain.island;

import io.tofpu.speedbridge2.domain.common.database.wrapper.Database;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.common.util.DatabaseUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.domain.common.util.DatabaseUtil.runAsync;

public final class IslandDatabase extends Database {
    public IslandDatabase() {
        super(DatabaseTable.of("islands", "slot NOT NULL PRIMARY KEY", "category TEXT", "schematic_name TEXT", "spawn_point TEXT"));
    }

    public @NotNull CompletableFuture<Void> insert(final Island island) {
        return DatabaseUtil.databaseQueryExecute("INSERT OR IGNORE INTO islands VALUES " +
                                                 "(?, ?, ?, ?)", databaseQuery -> {
            databaseQuery.setInt(island.getSlot());
            databaseQuery.setString(island.getCategory());
            databaseQuery.setString(island.getSchematicName());
            databaseQuery.setString(locationToString(island.getAbsoluteLocation()));
        });
    }

    public @NotNull CompletableFuture<Void> update(final Island island) {
        return DatabaseUtil.databaseQueryExecute(
                "UPDATE islands SET category = ?, schematic_name = " +
                "?, spawn_point = ? WHERE slot = ?", databaseQuery -> {
                    BridgeUtil.debug("island category: " + island.getCategory());
                    databaseQuery.setString(island.getCategory());

                    BridgeUtil.debug("island schematic: " + island.getSchematicName());
                    databaseQuery.setString(island.getSchematicName());

                    final Location spawnPoint = island.getAbsoluteLocation();
                    if (spawnPoint != null) {
                        databaseQuery.setString(locationToString(spawnPoint));
                    } else {
                        databaseQuery.setString(null);
                    }

                    databaseQuery.setInt(island.getSlot());
                });
    }

    public @NotNull CompletableFuture<Void> delete(final int slot) {
        return DatabaseUtil.databaseQueryExecute("DELETE FROM islands WHERE slot = ?", databaseQuery -> {
            databaseQuery.setInt(slot);
        });
    }

    public @NotNull CompletableFuture<List<Island>> getStoredIslands() {
        return runAsync(() -> {
            final List<Island> islands = new ArrayList<>();

            try {
                DatabaseUtil.databaseQuery("SELECT * FROM islands", resultSet -> {
                    while (resultSet.next()) {
                        final Island island = new Island(resultSet.getInt("slot"), resultSet.getString("category"));
                        island.selectSchematic(resultSet.getString("schematic_name"));

                        final String spawnPoint = resultSet.getString("spawn_point");
                        if (spawnPoint != null) {
                            final String[] split = spawnPoint.split(":");
                            final int x = Integer.parseInt(split[0]);
                            final int y = Integer.parseInt(split[1]);
                            final int z = Integer.parseInt(split[2]);
                            final float yaw = Float.parseFloat(split[3]);
                            final float pitch = Float.parseFloat(split[4]);

                            island.setAbsoluteLocation(new Location(Bukkit.getWorld("Speedbridge2"), x, y, z, yaw, pitch));
                        }

                        islands.add(island);
                    }
                        })
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return islands;
        });
    }

    private String locationToString(final Location location) {
        if (location == null) {
            return null;
        }

        return location.getBlockX() + ":" + location.getBlockY() + ":" +
               location.getBlockZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }
}
