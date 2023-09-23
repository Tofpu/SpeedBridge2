package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;
import com.github.tofpu.speedbridge2.database.factory.session.SessionFactoryMaker;

public class DatabaseBuilder {

    private final String packageName;

    private DatabaseBuilder(String packageName) {
        this.packageName = packageName;
    }

    public static DatabaseBuilder create(String packageName) {
        return new DatabaseBuilder(packageName);
    }

    public <T extends Database> T build(DriverOptions driverOptions, DatabaseFactory<T> factory) {
        return factory.create(
            SessionFactoryMaker.create(packageName, driverOptions));
    }
}
