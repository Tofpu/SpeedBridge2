package com.github.tofpu.speedbridge2.database;

import org.hibernate.Session;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public interface AsyncDatabase extends Database {
    CompletableFuture<Void> executeAsync(Consumer<Session> sessionConsumer);

    @Override
    default boolean supportsAsync() {
        return true;
    }
}