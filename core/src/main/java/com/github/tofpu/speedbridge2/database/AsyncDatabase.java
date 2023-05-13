package com.github.tofpu.speedbridge2.database;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.hibernate.Session;

public interface AsyncDatabase extends Database {

    CompletableFuture<Void> executeAsync(Consumer<Session> sessionConsumer);

    @Override
    default boolean supportsAsync() {
        return true;
    }
}