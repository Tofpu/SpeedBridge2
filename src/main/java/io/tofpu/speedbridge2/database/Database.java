package io.tofpu.speedbridge2.database;

import io.tofpu.speedbridge2.database.manager.DatabaseManager;
import io.tofpu.speedbridge2.database.wrapper.DatabaseTable;

import java.sql.Connection;

public class Database {
    public Database(final DatabaseTable table) {
        DatabaseManager.appendTable(table);
    }

    public Connection getConnection() {
        return DatabaseManager.getConnection();
    }
}
