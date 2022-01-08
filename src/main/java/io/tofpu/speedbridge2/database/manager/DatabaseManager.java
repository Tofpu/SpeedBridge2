package io.tofpu.speedbridge2.database.manager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.tofpu.speedbridge2.database.Databases;
import io.tofpu.speedbridge2.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.database.wrapper.DatabaseTable;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import static io.tofpu.speedbridge2.database.util.DatabaseUtil.runAsync;

public class DatabaseManager {
    private static final Queue<String> TABLE_QUEUE = new LinkedList<>();
    private static HikariDataSource dataSource;

    public static CompletableFuture<Void> load(final Plugin plugin) {
        return runAsync(() -> {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            final HikariConfig config = new HikariConfig();
            final File file = new File(plugin.getDataFolder(), "data.db");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            config.setJdbcUrl("jdbc:sqlite:" + file);
            config.setUsername("test");
            config.setPassword("test");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.setConnectionTestQuery("SELECT 1;");

            dataSource = new HikariDataSource(config);

            loadTables();
        });
    }

    private static void loadTables() {
        // a hacky way to call the class's constructor
        Databases.ISLAND_DATABASE.toString();

        for (final String table : TABLE_QUEUE) {
            try (final DatabaseQuery query = new DatabaseQuery(table)) {
                query.execute();
                System.out.println("Executed " + table + " table initiation!");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void shutdown() {
        dataSource.close();
    }

    public static void appendTable(final DatabaseTable table) {
        TABLE_QUEUE.add(table.toString());
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
