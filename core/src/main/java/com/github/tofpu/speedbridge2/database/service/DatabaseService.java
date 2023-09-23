package com.github.tofpu.speedbridge2.database.service;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.database.AsyncDatabase;
import com.github.tofpu.speedbridge2.database.Database;
import com.github.tofpu.speedbridge2.database.DatabaseBuilder;
import com.github.tofpu.speedbridge2.database.DatabaseFactoryMaker;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import com.github.tofpu.speedbridge2.database.driver.type.H2DriverOptions;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;
import com.github.tofpu.speedbridge2.service.LoadableService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.Session;

public class DatabaseService implements LoadableService {

    private final DatabaseFactory<?> databaseFactory;
    private final DriverOptions driverOptions;
    private Database database;

    public DatabaseService() {
        this(H2DriverOptions.create());
    }

    public DatabaseService(DriverOptions driverOptions) {
        this(DatabaseFactoryMaker.async(Executors.newSingleThreadExecutor()), driverOptions);
    }

    public DatabaseService(DatabaseFactory<?> databaseFactory, DriverOptions driverOptions) {
        this.databaseFactory = databaseFactory;
        this.driverOptions = driverOptions;
    }

    @Override
    public void load() {
        this.database = DatabaseBuilder.create("com.github.tofpu.speedbridge2")
            .build(driverOptions, databaseFactory);
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

    public Database database() {
        return database;
    }
}
