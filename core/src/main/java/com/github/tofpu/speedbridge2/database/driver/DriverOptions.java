package com.github.tofpu.speedbridge2.database.driver;

public interface DriverOptions {

    static DriverOptions createH2(ConnectionType connectionType) {
        return new H2DriverOptions(connectionType);
    }

    static DriverOptions createH2() {
        return createH2(ConnectionType.MEMORY);
    }

    static DriverOptions createMySQL(String host, String database, String username, String password) {
        return new MySQLDriverOptions(host, database, username, password);
    }

    String connectionUrl();

    String dialect();

    String driverClass();

    String username();
    String password();
}
