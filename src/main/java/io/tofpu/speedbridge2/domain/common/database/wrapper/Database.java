package io.tofpu.speedbridge2.domain.common.database.wrapper;

import io.tofpu.speedbridge2.domain.common.database.DatabaseManager;

import java.sql.Connection;

public class Database {
    public Database(final DatabaseTable table) {
        DatabaseManager.appendTable(table);
    }

    public Connection getConnection() {
        return DatabaseManager.getConnection();
    }
}
