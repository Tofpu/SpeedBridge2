package com.github.tofpu.speedbridge2.database.driver;

class H2DriverOptions implements DriverOptions {
    private final ConnectionType connectionType;

    public H2DriverOptions(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public String connectionUrl() {
        String url = "jdbc:h2";
        switch (connectionType) {
            case MEMORY:
                return url + ":mem:";
            case FILE:
                return url + ":file:storage.db";
            case REMOTE:
                throw new UnsupportedOperationException(
                        "H2 driver does not support remote connection capabilities.");
        }
        throw new IllegalArgumentException("Unknown connection type: " + connectionType);
    }

    @Override
    public String dialect() {
        return "org.hibernate.dialect.H2Dialect";
    }

    @Override
    public String driverClass() {
        return "org.h2.Driver";
    }

    @Override
    public String username() {
        return null;
    }

    @Override
    public String password() {
        return null;
    }
}
