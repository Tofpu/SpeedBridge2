package io.tofpu.speedbridge2.database.infra.db;

import io.tofpu.speedbridge2.database.infra.connection.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public class Database {
    private final ConnectionProvider connectionProvider;

    public Database(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void handle(Consumer<Connection> consumer) {
        try (Connection connection = connectionProvider.provideConnection()) {
            consumer.accept(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public <T> T supply(Function<Connection, T> function) {
        AtomicReference<T> result = new AtomicReference<>();
        handle(connection -> {
            try {
                result.set(function.apply(connection));
            } catch (Exception e) {
                throw new RuntimeException("Function execution failed", e);
            }
        });
        return result.get();
    }
}
