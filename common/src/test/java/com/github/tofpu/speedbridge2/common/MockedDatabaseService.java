package com.github.tofpu.speedbridge2.common;

import com.github.tofpu.speedbridge2.database.Database;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import org.hibernate.Session;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.Mockito.mock;

public class MockedDatabaseService extends DatabaseService {
    @Override
    public void load() {
        // do nothing
    }

    @Override
    public void unload() {
        // do nothing
    }

    @Override
    public void executeSync(Consumer<Session> sessionConsumer) {
        sessionConsumer.accept(mock());
    }

    @Override
    public CompletableFuture<Void> executeAsync(Consumer<Session> sessionConsumer) {
        sessionConsumer.accept(mock());
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public <T> CompletableFuture<T> computeAsync(Function<Session, T> sessionFunction) {
        return CompletableFuture.completedFuture(sessionFunction.apply(mock()));
    }

    @Override
    public Database database() {
        return mock();
    }
}
