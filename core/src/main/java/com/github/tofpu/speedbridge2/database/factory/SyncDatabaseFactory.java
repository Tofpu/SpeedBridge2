package com.github.tofpu.speedbridge2.database.factory;

import com.github.tofpu.speedbridge2.database.Database;
import com.github.tofpu.speedbridge2.database.impl.StandardSyncDatabase;
import org.hibernate.SessionFactory;

public class SyncDatabaseFactory extends DatabaseFactory<Database> {
    @Override
    public Database create(SessionFactory sessionFactory) {
        return new StandardSyncDatabase(sessionFactory);
    }
}
