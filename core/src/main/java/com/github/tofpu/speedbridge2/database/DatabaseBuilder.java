package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.database.driver.ConnectionDetails;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;
import com.github.tofpu.speedbridge2.database.factory.session.SessionFactoryMaker;

public class DatabaseBuilder {
    private final String packageName;
    private ConnectionDetails connectionDetails;

    private DatabaseBuilder(String packageName) {
        this.packageName = packageName;
    }

    public static DatabaseBuilder create(String packageName) {
        return new DatabaseBuilder(packageName);
    }

    public DatabaseBuilder data(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
        return this;
    }

    public <T extends Database> T build(DatabaseType databaseType, DatabaseFactory<T> factory) {
        return factory.create(SessionFactoryMaker.create(packageName, databaseType.getDriverOptions(), connectionDetails));
    }
}
