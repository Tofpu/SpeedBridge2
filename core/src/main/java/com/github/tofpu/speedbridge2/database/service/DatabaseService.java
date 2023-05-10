package com.github.tofpu.speedbridge2.database.service;

import com.github.tofpu.speedbridge2.database.*;
import com.github.tofpu.speedbridge2.database.driver.ConnectionDetails;
import com.github.tofpu.speedbridge2.service.LoadableService;
import org.hibernate.Session;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class DatabaseService implements LoadableService {
    private final ConnectionDetails details;
    private final OperationType operationType;
    private final DatabaseType databaseType;

    public DatabaseService() {
        this(ConnectionDetails.MEMORY, OperationType.ASYNC, DatabaseType.H2);
    }

    public DatabaseService(ConnectionDetails details, OperationType operationType, DatabaseType databaseType) {
        this.details = details;
        this.operationType = operationType;
        this.databaseType = databaseType;
    }

    private Database database;

    @Override
    public void load() {
        this.database = DatabaseBuilder.create("com.github.tofpu.speedbridge2")
                .data(details)
                .operationType(operationType)
                .build(databaseType);
    }

    @Override
    public void unload() {
        this.database.shutdown();
    }

    public void compute(final Consumer<Session> sessionConsumer) {
        this.database.compute(sessionConsumer);
    }

    public <T> CompletableFuture<T> computeAsync(final Function<Session, T> sessionFunction) {
        requireState(supportsAsync(), "Async operations is not supported.");
        AsyncDatabase asyncDatabase = (AsyncDatabase) this.database;
        return asyncDatabase.computeAsync(sessionFunction);
    }

    public <T> CompletableFuture<T> executeAsync(final Consumer<Session> sessionConsumer) {
        return computeAsync(session -> {
            sessionConsumer.accept(session);
            return null;
        });
    }

    public boolean supportsAsync() {
        return this.database.supportsAsync();
    }
}
