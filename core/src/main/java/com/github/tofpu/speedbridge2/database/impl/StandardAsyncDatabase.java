package com.github.tofpu.speedbridge2.database.impl;

import com.github.tofpu.speedbridge2.database.AsyncDatabase;
import com.github.tofpu.speedbridge2.database.Database;
import org.hibernate.Session;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class StandardAsyncDatabase implements Database, AsyncDatabase {
    private final Database delegate;
    private final ExecutorService executor;

    public DefaultAsyncDatabase(Database delegate) {
        this.delegate = delegate;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(Consumer<Session> sessionConsumer) {
        delegate.execute(sessionConsumer);
    }

    @Override
    public CompletableFuture<Void> executeAsync(Consumer<Session> sessionConsumer) {
        return CompletableFuture.runAsync(() -> execute(sessionConsumer), executor);
    }

    @Override
    public ExecutorService executor() {
        return executor;
    }

    @Override
    public void shutdown() {
        System.out.println("Attempting to shut down...");
        executor.shutdown();
        try {
            boolean awaitTermination = executor.awaitTermination(10, TimeUnit.SECONDS);
            if (awaitTermination) {
                System.out.println("Terminated successfully");
            } else {
                System.out.println("It took too long, the 10 seconds timeout has elapsed...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        delegate.shutdown();
    }
}
