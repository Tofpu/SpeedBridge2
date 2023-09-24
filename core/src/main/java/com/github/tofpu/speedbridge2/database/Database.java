package com.github.tofpu.speedbridge2.database;

import org.hibernate.Session;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Database {
    static SimpleDatabaseFactory factory() {
        return DatabaseFactoryHolder.INSTANCE;
    }

    static DatabaseBuilder builder() {
        return new DatabaseBuilder();
    }

    CompletableFuture<Void> executeAsync(Consumer<Session> sessionConsumer);

    void executeSync(Consumer<Session> sessionConsumer);

    void shutdown();

    class DatabaseFactoryHolder {
        private static final SimpleDatabaseFactory INSTANCE = new SimpleDatabaseFactory();
    }
}