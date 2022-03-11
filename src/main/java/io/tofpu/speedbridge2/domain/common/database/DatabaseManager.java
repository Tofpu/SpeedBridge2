package io.tofpu.speedbridge2.domain.common.database;

import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseTable;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
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
    private static File databaseFile;
    private static Connection connection;

    public static CompletableFuture<Void> load(final @NotNull Plugin plugin) {
        return PluginExecutor.runAsync(() -> {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            final File parentFolder = plugin.getDataFolder();
            if (!parentFolder.exists()) {
                parentFolder.mkdir();
            }

            databaseFile = new File(plugin.getDataFolder(), "data.db");
            try {
                databaseFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connection = getConnection();

            loadTables();
        });
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
            try (final DatabaseQuery query = new DatabaseQuery(table)) {
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
            return DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (SQLException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
