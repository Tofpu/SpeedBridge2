package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.database.driver.ConnectionDetails;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class DatabaseBuilder {
    private final String packageName;
    private OperationType operationType = OperationType.SYNC;
    private ConnectionDetails connectionDetails;

    private DatabaseBuilder(String packageName) {
        this.packageName = packageName;
    }

    public static DatabaseBuilder create(String packageName) {
        return new DatabaseBuilder(packageName);
    }

    public DatabaseBuilder operationType(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public DatabaseBuilder data(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
        return this;
    }

    public Database build(DatabaseType databaseType) {
        requireState(connectionDetails != null, "ConnectionData must be set");
        return new DatabaseFactory().construct(packageName, databaseType, operationType, connectionDetails);
    }
}
