package io.tofpu.speedbridge2.database.storage;

import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import io.tofpu.speedbridge2.repository.storage.SQLStorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * A SQLite implementation for {@link SQLStorage}.
 */
public class SQLiteStorage extends BaseStorage {
    private Connection connection;

    public SQLiteStorage() {
        super(1);
    }

    @Override
    public CompletableFuture<Void> init() {
        return asyncThreadExecutor().runAsync(() -> {
                    try {
                        Class.forName("org.sqlite.JDBC");
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .thenRun(this::establishConnection);
    }

    @Override
    public void establishConnection() {
        synchronized (this) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }
}
