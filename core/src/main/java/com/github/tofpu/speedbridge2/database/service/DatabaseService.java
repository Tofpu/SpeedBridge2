package com.github.tofpu.speedbridge2.database.service;

import com.github.tofpu.speedbridge2.database.Database;
import com.github.tofpu.speedbridge2.database.DatabaseFactoryMaker;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;
import com.github.tofpu.speedbridge2.service.LoadableService;
import org.hibernate.Session;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseService implements LoadableService {

    private final DatabaseFactory<?> databaseFactory;
    private final DriverOptions driverOptions;
    private Database database;

    public DatabaseService() {
        this(DriverOptions.createH2());
    }

    public DatabaseService(DriverOptions driverOptions) {
        this(DatabaseFactoryMaker.createStandardDatabaseFactory(Executors.newSingleThreadExecutor()), driverOptions);
    }

    public DatabaseService(DatabaseFactory<?> databaseFactory, DriverOptions driverOptions) {
        this.databaseFactory = databaseFactory;
        this.driverOptions = driverOptions;
    }

    @Override
    public void load() {
        this.database = Database.builder()
                .factory(databaseFactory)
                .build(driverOptions);
    }

    @Override
    public void unload() {
        this.database.shutdown();
    }

    public void executeSync(final Consumer<Session> sessionConsumer) {
        this.database.executeSync(sessionConsumer);
    }

    @SuppressWarnings("unchecked")
    public <T> T computeSync(Function<Session, T> sessionFunction) {
        final Object[] result = new Object[1];
        executeSync(session -> result[0] = sessionFunction.apply(session));
        return (T) result[0];
    }

    public <T> CompletableFuture<T> computeAsync(Function<Session, T> sessionFunction) {
        CompletableFuture<T> future = new CompletableFuture<>();
        executeAsync(session -> {
            try {
                future.complete(sessionFunction.apply(session));
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public CompletableFuture<Void> executeAsync(final Consumer<Session> sessionConsumer) {
        return this.database.executeAsync(sessionConsumer);
    }

    public Database database() {
        return database;
    }
}
