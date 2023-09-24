package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.database.driver.ConnectionType;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;

import java.util.concurrent.Executors;

public class SimpleDatabaseFactory {
    DatabaseBuilder createBasicBuilder() {
        return Database.builder().factory(createDatabaseFactory());
    }

    DatabaseFactory<?> createDatabaseFactory() {
        return DatabaseFactoryMaker.createStandardDatabaseFactory(Executors.newSingleThreadExecutor());
    }

    public Database createDatabase(DriverOptions driverOptions) {
        return createBasicBuilder().build(driverOptions);
    }

    public Database createH2Database() {
        return createH2Database(ConnectionType.MEMORY);
    }

    public Database createH2Database(ConnectionType connectionType) {
        return createDatabase(DriverOptions.createH2(connectionType));
    }

    public Database createMySQLDatabase(String host, String database, String username, String password) {
        return createDatabase(DriverOptions.createMySQL(host, database, username, password));
    }
}
