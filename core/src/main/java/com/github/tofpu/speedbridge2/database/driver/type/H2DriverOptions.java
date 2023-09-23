package com.github.tofpu.speedbridge2.database.driver.type;

import com.github.tofpu.speedbridge2.configuration.database.driver.H2DriverConfiguration;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;

public class H2DriverOptions implements DriverOptions {
    private final H2DriverConfiguration configuration;

    public static H2DriverOptions create() {
        return new H2DriverOptions(new H2DriverConfiguration());
    }

    public H2DriverOptions(H2DriverConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String connectionUrl() {
        String url = "jdbc:h2";
        switch (configuration.connectionType()) {
            case MEMORY:
                return url + ":mem:";
            case FILE:
                return url + ":file:storage.db";
            case REMOTE:
                throw new UnsupportedOperationException(
                        "H2 driver does not support remote connection capabilities.");
        }
        throw new IllegalArgumentException("Unknown connection type: " + configuration.connectionType());
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
