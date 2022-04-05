package io.tofpu.speedbridge2.model.common.database;

import com.google.common.io.Files;
import io.tofpu.speedbridge2.model.common.PluginExecutor;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public final class DatabaseManager {
    private static final @NotNull Queue<String> TABLE_QUEUE = new LinkedList<>();
    private static File storageFile;
    private static Connection connection;

    public static CompletableFuture<Void> loadAsync(final @NotNull Plugin plugin) {
        return PluginExecutor.runAsync(() -> {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            final File parentFolder = new File(plugin.getDataFolder(), "data");
            storageFile = new File(parentFolder, "storage.db");
            if (!parentFolder.exists()) {
                parentFolder.mkdirs();
            }

            // if the server used a version anything lower
            // than 1.0.8, then the migration process will be executed
            migrateDataFile(plugin.getDataFolder(), storageFile);

            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }

            connection = getConnection();

            loadTables();
        });
    }

    private static void migrateDataFile(final File fromDirectory, final File toFile) {
        final File previousFile = new File(fromDirectory, "data.db");
        if (previousFile.exists()) {
            try {
                Files.move(previousFile, toFile);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static void loadTables() {
        while (getConnection() == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        // a hacky way to call the class's constructor
        Databases.ISLAND_DATABASE.toString();
        Databases.PLAYER_DATABASE.toString();
        Databases.STATS_DATABASE.toString();

        for (final String table : TABLE_QUEUE) {
            try (final DatabaseQuery query = DatabaseQuery.query(table)) {
                query.execute();
                BridgeUtil.debug("Attempted to create " + table + " table!");
            } catch (final Exception exception) {
                throw new IllegalStateException(exception);
            }
        }
    }

    public static void shutdown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static void appendTable(final @NotNull DatabaseTable table) {
        TABLE_QUEUE.add(table.toString());
    }

    public static @Nullable Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            return DriverManager.getConnection("jdbc:sqlite:" + storageFile);
        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
