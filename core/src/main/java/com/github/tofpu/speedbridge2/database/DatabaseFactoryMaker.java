package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.database.factory.AsyncDatabaseFactory;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;
import com.github.tofpu.speedbridge2.database.factory.SyncDatabaseFactory;
import java.util.concurrent.ExecutorService;

public class DatabaseFactoryMaker {

    public static DatabaseFactory<Database> sync() {
        return new SyncDatabaseFactory();
    }

    public static DatabaseFactory<AsyncDatabase> async(ExecutorService executor) {
        return new AsyncDatabaseFactory(sync(), executor);
    }
}
