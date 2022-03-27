package io.tofpu.speedbridge2.model.common.util;

import io.tofpu.speedbridge2.model.common.PluginExecutor;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseSet;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DatabaseUtil {
    /**
     * It takes a SQL query and a consumer that will be used to set up the query. It then
     * executes the query and returns a CompletableFuture that will be completed when the
     * query is done
     *
     * @param sql The SQL statement to execute.
     * @param databaseQueryConsumer A Consumer that takes a DatabaseQuery.
     * @return Nothing.
     */
    public static CompletableFuture<Void> databaseQueryExecute(final String sql, final Consumer<DatabaseQuery> databaseQueryConsumer) {
        return runAsync(() -> {
            try (final DatabaseQuery query = DatabaseQuery.query(sql)) {
                databaseQueryConsumer.accept(query);
                query.execute();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * It runs the given runnable on the main thread
     *
     * @param runnable The Runnable to run.
     * @return Nothing.
     */
    public static CompletableFuture<Void> runAsync(final Runnable runnable) {
        return PluginExecutor.runAsync(runnable);
    }

    /**
     * It takes a SQL query and a consumer that will be called for each row of the query
     *
     * @param sql The SQL statement to execute.
     * @param databaseQueryConsumer A consumer that will be called for each row of the query.
     * @return Nothing.
     */
    public static CompletableFuture<Void> databaseQuery(final String sql,
            final Consumer<DatabaseSet> databaseQueryConsumer) {
        return runAsync(() -> {
            try (final DatabaseQuery query = DatabaseQuery.query(sql)) {
                query.executeQuery(databaseQueryConsumer);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * It takes a supplier and returns a CompletableFuture
     *
     * @param supplier A function that returns a value.
     * @return A CompletableFuture<T>
     */
    public static <T> CompletableFuture<T> runAsync(final Supplier<?> supplier) {
        return PluginExecutor.supply(supplier);
    }
}
