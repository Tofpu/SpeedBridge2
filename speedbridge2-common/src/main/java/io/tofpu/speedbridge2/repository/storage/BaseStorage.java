package io.tofpu.speedbridge2.repository.storage;

import io.tofpu.speedbridge2.async.AsyncThreadExecutor;
import io.tofpu.speedbridge2.async.DefaultAsyncThreadExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * A ready-to-use abstract {@link ParentStorage} class
 * with {@link AsyncThreadExecutor} included.
 *
 * @see ParentStorage
 */
public abstract class BaseStorage implements ParentStorage {
    private final AsyncThreadExecutor asyncThreadExecutor;

    public BaseStorage(final int corePoolSize) {
        this.asyncThreadExecutor = new DefaultAsyncThreadExecutor(corePoolSize);
    }

    public CompletableFuture<Void> executeAsync(final String sql) {
        return asyncThreadExecutor().runAsync(() -> execute(sql));
    }

    public void execute(final String sql) {
        final Connection connection = getConnection();
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public abstract CompletableFuture<Void> init();

    @Override
    public abstract void establishConnection();

    @Override
    public abstract Connection getConnection();

    public AsyncThreadExecutor asyncThreadExecutor() {
        return asyncThreadExecutor;
    }
}