package io.tofpu.speedbridge2.database.util;

import io.tofpu.speedbridge2.database.wrapper.DatabaseQuery;

import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DatabaseUtil {
    public static CompletableFuture<Void> databaseQueryExecute(final String sql, final Consumer<DatabaseQuery> databaseQueryConsumer) {
        return runAsync(() -> {
            try (final DatabaseQuery query = new DatabaseQuery(sql)) {
                databaseQueryConsumer.accept(query);
                query.execute();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Void> runAsync(final Runnable runnable) {
        return CompletableFuture.runAsync(runnable);
    }

    public static CompletableFuture<Void> databaseQuery(final String sql, final Consumer<ResultSet> databaseQueryConsumer) {
        return runAsync(() -> {
            try (final DatabaseQuery query = new DatabaseQuery(sql)) {
                try (final ResultSet resultSet = query.executeQuery()) {
                    databaseQueryConsumer.accept(resultSet);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static <T> CompletableFuture<T> runAsync(final Supplier<?> supplier) {
        return (CompletableFuture<T>) CompletableFuture.supplyAsync(supplier);
    }
}
