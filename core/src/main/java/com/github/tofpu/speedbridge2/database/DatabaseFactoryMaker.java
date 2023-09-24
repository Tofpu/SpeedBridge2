package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.database.factory.StandardDatabaseFactory;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;

import java.util.concurrent.ExecutorService;

public class DatabaseFactoryMaker {

    public static DatabaseFactory<?> createStandardDatabaseFactory(ExecutorService executorService) {
        return new StandardDatabaseFactory(executorService);
    }
}
