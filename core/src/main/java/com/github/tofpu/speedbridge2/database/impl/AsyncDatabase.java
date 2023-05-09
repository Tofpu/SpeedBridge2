package com.github.tofpu.speedbridge2.database.impl;

import com.github.tofpu.speedbridge2.database.Database;
import org.hibernate.Session;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AsyncDatabase implements Database {
    private final Database delegate;
    private final ExecutorService executorService;

    public AsyncDatabase(Database delegate) {
        this.delegate = delegate;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void compute(Consumer<Session> sessionConsumer) {
        executorService.execute(() -> {
            try {
                delegate.compute(sessionConsumer);
            } catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        });
    }

    @Override
    public void shutdown() {
        System.out.println("Attempting to shut down...");
        executorService.shutdown();
        try {
            boolean awaitTermination = executorService.awaitTermination(10, TimeUnit.SECONDS);
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
