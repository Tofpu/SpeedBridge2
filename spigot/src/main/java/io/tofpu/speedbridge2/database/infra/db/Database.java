package io.tofpu.speedbridge2.database.infra.db;

import io.tofpu.speedbridge2.database.infra.connection.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public class Database {
    private final ConnectionProvider connectionProvider;
    private final ExecutorService executorService;

    public Database(ConnectionProvider connectionProvider, ExecutorService executorService) {
        this.connectionProvider = connectionProvider;
        this.executorService = executorService;
    }

    public CompletableFuture<Void> handleAsync(Consumer<Connection> consumer) {
        return CompletableFuture.runAsync(() -> handle(consumer), executorService);
    }

    public <T> CompletableFuture<T> supplyAsync(Function<Connection, T> function) {
        return CompletableFuture.supplyAsync(() -> supply(function), executorService);
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
