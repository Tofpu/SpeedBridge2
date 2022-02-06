package io.tofpu.speedbridge2.domain.common.database.wrapper;

import io.tofpu.speedbridge2.domain.common.database.DatabaseManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

public class Database {
    public Database(final @NotNull DatabaseTable table) {
        DatabaseManager.appendTable(table);
    }

    public @Nullable Connection getConnection() {
        return DatabaseManager.getConnection();
    }
}
