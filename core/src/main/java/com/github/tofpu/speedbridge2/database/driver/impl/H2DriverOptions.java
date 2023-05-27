package com.github.tofpu.speedbridge2.database.driver.impl;

import com.github.tofpu.speedbridge2.database.driver.ConnectionDetails;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;

public class H2DriverOptions implements DriverOptions {

    @Override
    public String connectionUrl(ConnectionDetails data) {
        String url = "jdbc:h2";
        switch (data.getType()) {
            case MEMORY:
                return url + ":mem:";
            case FILE:
                return url + ":file:" + data.getData();
            case REMOTE:
                throw new UnsupportedOperationException(
                    "Remote support has not been implemented yet.");
        }
        throw new IllegalArgumentException("Unknown connection type: " + data.getType());
    }

    @Override
    public String dialect() {
        return "org.hibernate.dialect.H2Dialect";
    }

    @Override
    public String driverClass() {
        return "org.h2.Driver";
    }
}
