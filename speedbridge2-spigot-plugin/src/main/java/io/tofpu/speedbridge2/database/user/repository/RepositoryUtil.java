package io.tofpu.speedbridge2.database.user.repository;

import io.tofpu.speedbridge2.database.storage.StorageUtil;
import io.tofpu.speedbridge2.repository.storage.BaseStorage;
import io.tofpu.speedbridge2.sql.ResultRetrieval;
import io.tofpu.speedbridge2.sql.StatementQuery;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static io.tofpu.speedbridge2.sql.StatementQuery.query;
import static org.apache.commons.lang.Validate.notNull;

/**
 * A repository-based convenience utility class.
 */
public class RepositoryUtil {
    public static <T> CompletableFuture<T> simpleFetch(
            final BaseStorage storage, final String fetchSQL, final UUID key,
            final Function<ResultRetrieval, T> singleSetFunction) {
        notNull(storage);
        notNull(key);
        notNull(fetchSQL);
        notNull(singleSetFunction);

        return storage.asyncThreadExecutor()
                .supplyAsync(() -> {
                    return query(storage.getConnection(), fetchSQL).setBlob(1, StorageUtil.uidAsByte(key))
                            .execute()
                            .returnSingleSet(singleSetFunction);
                });
    }

    public static <T> CompletableFuture<T> complexFetch(
            final BaseStorage storage, final String fetchSQL, final UUID key,
            final Function<StatementQuery, StatementQuery> statementQueryFunction,
            final Function<ResultRetrieval, T> singleSetFunction) {
        notNull(storage);
        notNull(key);
        notNull(fetchSQL);
        notNull(statementQueryFunction);
        notNull(singleSetFunction);

        return storage.asyncThreadExecutor()
                .supplyAsync(() -> {
                    final StatementQuery statementQuery =
                            query(storage.getConnection(), fetchSQL).setBlob(1, StorageUtil.uidAsByte(key));

                    return statementQueryFunction.apply(statementQuery)
                            .execute()
                            .returnSingleSet(singleSetFunction);
                });
    }

    public static CompletableFuture<Void> complexExecute(
            final BaseStorage storage, final String executionSQL, final UUID key,
            final Function<StatementQuery, StatementQuery> statementQueryFunction) {
        notNull(storage);
        notNull(key);
        notNull(executionSQL);
        notNull(statementQueryFunction);

        return storage.asyncThreadExecutor()
                .runAsync(() -> {
                    final StatementQuery statementQuery =
                            query(storage.getConnection(), executionSQL).setBlob(1, StorageUtil.uidAsByte(key));

                    statementQueryFunction.apply(statementQuery)
                            .execute();
                });
    }

    public static CompletableFuture<Void> simpleExecute(
            final BaseStorage storage, final String executionSQL, final UUID key) {
        notNull(storage);
        notNull(key);
        notNull(executionSQL);

        return storage.asyncThreadExecutor()
                .runAsync(() -> {
                    query(storage.getConnection(), executionSQL).setBlob(1, StorageUtil.uidAsByte(key))
                            .execute();
                });
    }
}
