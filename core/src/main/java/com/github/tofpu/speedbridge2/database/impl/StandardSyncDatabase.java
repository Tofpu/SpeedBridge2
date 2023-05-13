package com.github.tofpu.speedbridge2.database.impl;

import com.github.tofpu.speedbridge2.database.Database;
import java.util.function.Consumer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class StandardSyncDatabase implements Database {

    private final SessionFactory factory;

    public StandardSyncDatabase(final SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void execute(Consumer<Session> sessionConsumer) {
        factory.inTransaction(sessionConsumer);
    }

    @Override
    public void shutdown() {
        factory.close();
    }
}
