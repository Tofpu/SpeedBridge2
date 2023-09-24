package com.github.tofpu.speedbridge2.database.factory;

import com.github.tofpu.speedbridge2.database.Database;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class StandardDatabaseFactory extends DatabaseFactory<Database> {
    private final ExecutorService executor;

    public StandardDatabaseFactory(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public Database create(SessionFactory sessionFactory) {
        return new StandardDatabase(sessionFactory, executor);
    }

    static class StandardDatabase implements Database {
        private final SessionFactory factory;
        private final ExecutorService executor;

        public StandardDatabase(SessionFactory factory, ExecutorService executor) {
            this.factory = factory;
            this.executor = executor;
        }

        @Override
        public CompletableFuture<Void> executeAsync(Consumer<Session> sessionConsumer) {
            return CompletableFuture.runAsync(() -> executeSync(sessionConsumer), executor);
        }

        @Override
        public void executeSync(Consumer<Session> sessionConsumer) {
            try (final Session session = factory.openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    sessionConsumer.accept(session);
                    if (!transaction.isActive()) {
                        throw new IllegalStateException();
                    }
                } catch (RuntimeException exception) {
                    if (transaction.isActive()) {
                        try {
                            transaction.rollback();
                        } catch (Exception ignored) {
                        }
                    }
                    throw exception;
                }

                transaction.commit();
            }
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
            factory.close();
        }
    }
}
