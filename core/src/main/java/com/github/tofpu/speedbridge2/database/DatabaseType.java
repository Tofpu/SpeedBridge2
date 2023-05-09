package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import com.github.tofpu.speedbridge2.database.driver.impl.H2DriverOptions;

public enum DatabaseType {
    H2(new H2DriverOptions());

    private final DriverOptions driverOptions;

    DatabaseType(DriverOptions driverOptions) {
        this.driverOptions = driverOptions;
    }

    public DriverOptions getDriverOptions() {
        return driverOptions;
    }
}
