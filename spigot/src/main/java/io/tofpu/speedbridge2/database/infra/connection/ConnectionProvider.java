package io.tofpu.speedbridge2.database.infra.connection;

import java.sql.Connection;

public interface ConnectionProvider {
    Connection provideConnection();
}
