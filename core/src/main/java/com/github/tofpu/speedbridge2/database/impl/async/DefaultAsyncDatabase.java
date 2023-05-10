package com.github.tofpu.speedbridge2.database.impl.async;

import com.github.tofpu.speedbridge2.database.AsyncDatabase;
import com.github.tofpu.speedbridge2.database.Database;
import org.hibernate.Session;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultAsyncDatabase implements Database, AsyncDatabase {
    private final Database delegate;
    private final ExecutorService executor;

    public DefaultAsyncDatabase(Database delegate) {
        this.delegate = delegate;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void compute(Consumer<Session> sessionConsumer) {
        delegate.compute(sessionConsumer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> computeAsync(Function<Session, T> sessionFunction) {
        return CompletableFuture.supplyAsync(() -> {
            final Object[] result = new Object[1];
            try {
                delegate.compute(session -> {
                    result[0] = sessionFunction.apply(session);
                });
                return (T) result[0];
            } catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        }, executor);
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
