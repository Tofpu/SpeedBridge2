package com.github.tofpu.speedbridge2.database.service;

import com.github.tofpu.speedbridge2.database.*;
import com.github.tofpu.speedbridge2.database.driver.ConnectionDetails;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;
import com.github.tofpu.speedbridge2.service.LoadableService;
import org.hibernate.Session;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class DatabaseService implements LoadableService {
    private final ConnectionDetails details;
    private final DatabaseFactory<?> databaseFactory;
    private final DatabaseType databaseType;

    public DatabaseService() {
        this(ConnectionDetails.MEMORY, DatabaseFactoryMaker.async(Executors.newSingleThreadExecutor()), DatabaseType.H2);
    }

    public DatabaseService(ConnectionDetails details, DatabaseFactory<?> databaseFactory, DatabaseType databaseType) {
        this.details = details;
        this.databaseFactory = databaseFactory;
        this.databaseType = databaseType;
    }

    private Database database;

    @Override
    public void load() {
        this.database = DatabaseBuilder.create("com.github.tofpu.speedbridge2")
                .data(details)
                .build(databaseType, databaseFactory);
    }

    @Override
    public void unload() {
        this.database.shutdown();
    }

    public void execute(final Consumer<Session> sessionConsumer) {
        this.database.execute(sessionConsumer);
    }

    @SuppressWarnings("unchecked")
    public <T> T compute(Function<Session, T> sessionFunction) {
        final Object[] result = new Object[1];
        execute(session -> result[0] = sessionFunction.apply(session));
        return (T) result[0];
    }

    public <T> CompletableFuture<T> computeAsync(Function<Session, T> sessionFunction) {
        requireState(supportsAsync(), "Async operations is not supported.");
        CompletableFuture<T> future = new CompletableFuture<>();
        executeAsync(session -> future.complete(sessionFunction.apply(session)));
        return future;
    }

    public CompletableFuture<Void> executeAsync(final Consumer<Session> sessionConsumer) {
        requireState(supportsAsync(), "Async operations is not supported.");
        AsyncDatabase asyncDatabase = (AsyncDatabase) this.database;
        return asyncDatabase.executeAsync(sessionConsumer);
    }

    public boolean supportsAsync() {
        return this.database.supportsAsync();
    }
}
