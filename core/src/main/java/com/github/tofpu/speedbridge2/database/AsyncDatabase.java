package com.github.tofpu.speedbridge2.database;

import org.hibernate.Session;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface AsyncDatabase extends Database {
    <T> CompletableFuture<T> computeAsync(final Function<Session, T> sessionFunction);

    @Override
    default boolean supportsAsync() {
        return true;
    }
}