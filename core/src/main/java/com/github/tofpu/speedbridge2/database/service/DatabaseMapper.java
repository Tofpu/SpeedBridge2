package com.github.tofpu.speedbridge2.database.service;

import com.github.tofpu.speedbridge2.configuration.database.DatabaseConfiguration;
import com.github.tofpu.speedbridge2.configuration.database.DatabaseDriverType;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import com.github.tofpu.speedbridge2.database.driver.type.H2DriverOptions;
import com.github.tofpu.speedbridge2.database.driver.type.MySQLDriverOptions;

public class DatabaseMapper {
    public static DriverOptions map(final DatabaseConfiguration databaseConfiguration) {
        DatabaseDriverType driverType = databaseConfiguration.currentDriverType();
        switch (driverType) {
            case H2:
                return new H2DriverOptions(databaseConfiguration.get(driverType));
            case MYSQL:
                return new MySQLDriverOptions(databaseConfiguration.get(driverType));
            default:
                throw new IllegalStateException("Unknown driver type: " + driverType);
        }
    }
}
