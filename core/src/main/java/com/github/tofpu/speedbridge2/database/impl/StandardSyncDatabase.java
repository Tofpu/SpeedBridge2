package com.github.tofpu.speedbridge2.database.impl;

import com.github.tofpu.speedbridge2.database.Database;
import java.util.function.Consumer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class StandardSyncDatabase implements Database {

    private final SessionFactory factory;

    public StandardSyncDatabase(final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void execute(Consumer<Session> sessionConsumer) {
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
        factory.close();
    }
}
