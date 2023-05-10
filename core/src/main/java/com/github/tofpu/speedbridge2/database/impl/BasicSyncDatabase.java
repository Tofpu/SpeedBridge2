package com.github.tofpu.speedbridge2.database.impl;

import com.github.tofpu.speedbridge2.database.Database;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class BasicSyncDatabase implements Database {
    private final SessionFactory factory;

    public BasicSyncDatabase(final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void execute(Consumer<Session> sessionConsumer) {
        factory.inTransaction(sessionConsumer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T compute(Function<Session, T> sessionFunction) {
        final Object[] result = new Object[1];
        execute(session -> result[0] = sessionFunction.apply(session));
        return (T) result[0];
    }

    @Override
    public void shutdown() {
        factory.close();
    }
}
