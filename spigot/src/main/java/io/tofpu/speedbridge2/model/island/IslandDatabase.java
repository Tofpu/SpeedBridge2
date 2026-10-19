package io.tofpu.speedbridge2.model.island;

import io.tofpu.speedbridge2.model.common.database.wrapper.Database;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.common.util.DatabaseUtil;
import io.tofpu.speedbridge2.model.island.exception.IslandLoadFailureException;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.IslandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.model.common.util.DatabaseUtil.runAsync;

public final class IslandDatabase extends Database {
    public IslandDatabase() {
        super(DatabaseTable.of("islands", "slot NOT NULL PRIMARY KEY", "category TEXT", "schematic_name TEXT", "spawn_point TEXT"));
    }

    public @NotNull CompletableFuture<Void> insert(final Island island) {
        return DatabaseUtil.databaseExecute("INSERT OR IGNORE INTO islands VALUES " +
                "(?, ?, ?, ?)", databaseQuery -> {
            databaseQuery.setInt(island.getSlot());
            databaseQuery.setString(island.getCategory());
            databaseQuery.setString(island.getSchematicName());
            databaseQuery.setString(serialize(island.getAbsoluteLocation()));
        });
    }

    public @NotNull CompletableFuture<Void> update(final Island island) {
        return DatabaseUtil.databaseExecute(
                "UPDATE islands SET category = ?, schematic_name = " +
                        "?, spawn_point = ? WHERE slot = ?", databaseQuery -> {
                    BridgeUtil.debug("island category: " + island.getCategory());
                    databaseQuery.setString(island.getCategory());

                    BridgeUtil.debug("island schematic: " + island.getSchematicName());
                    databaseQuery.setString(island.getSchematicName());

                    final Location spawnPoint = island.getAbsoluteLocation();
                    if (spawnPoint != null) {
                        databaseQuery.setString(serialize(spawnPoint));
                    } else {
                        databaseQuery.setString(null);
                    }

                    databaseQuery.setInt(island.getSlot());
                });
    }

    public @NotNull CompletableFuture<Void> delete(final int slot) {
        return DatabaseUtil.databaseExecute("DELETE FROM islands WHERE slot = ?", databaseQuery -> {
            databaseQuery.setInt(slot);
        });
    }

    public @NotNull CompletableFuture<List<Island>> getStoredIslandsAsync() {
        return runAsync(() -> {
            final List<Island> islands = new ArrayList<>();

            try {
                DatabaseUtil.databaseQueryExecute("SELECT * FROM islands", resultSet -> {
                    while (resultSet.next()) {
                        final IslandBuilder builder = IslandBuilder.of();
                        builder.setSlot(resultSet.getInt("slot"));
                        builder.setCategory(resultSet.getString("category"));
                        builder.setSchematic(resultSet.getString("schematic_name"));

                        final String serializedSpawnPoint = resultSet.getString("spawn_point");
                        if (serializedSpawnPoint != null && !serializedSpawnPoint.isEmpty()) {
                            builder.setAbsoluteLocation(deserialize(serializedSpawnPoint));
                        }

                        islands.add(builder.build());
                    }
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IslandLoadFailureException(e);
            }

            return islands;
        });
    }

    private String serialize(final Location location) {
        if (location == null) {
            return null;
        }

        return location.getX() + ":" + location.getY() + ":" +
                location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    private Location deserialize(final String input) {
        if (input == null) {
            return null;
        }

        final String[] split = input.split(":");
        final double x = Double.parseDouble(split[0]);
        final double y = Double.parseDouble(split[1]);
        final double z = Double.parseDouble(split[2]);
        final float yaw = Float.parseFloat(split[3]);
        final float pitch = Float.parseFloat(split[4]);

        return new Location(Bukkit.getWorld("Speedbridge2"), x, y, z, yaw, pitch);
    }
}
