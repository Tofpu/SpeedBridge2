package com.github.tofpu.speedbridge2.database.factory;

import com.github.tofpu.speedbridge2.database.AsyncDatabase;
import com.github.tofpu.speedbridge2.database.Database;
import com.github.tofpu.speedbridge2.database.impl.StandardAsyncDatabase;
import org.hibernate.SessionFactory;

import java.util.concurrent.ExecutorService;

public class AsyncDatabaseFactory extends DatabaseFactory<AsyncDatabase> {
    private final DatabaseFactory<Database> delegate;
    private final ExecutorService executor;

    public AsyncDatabaseFactory(DatabaseFactory<Database> delegate, ExecutorService executor) {
        this.delegate = delegate;
        this.executor = executor;
    }

    @Override
    public AsyncDatabase create(SessionFactory sessionFactory) {
        return new StandardAsyncDatabase(delegate.create(sessionFactory), executor);
    }
}
