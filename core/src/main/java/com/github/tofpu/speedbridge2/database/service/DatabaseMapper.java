package com.github.tofpu.speedbridge2.database.service;

import com.github.tofpu.speedbridge2.configuration.database.DatabaseConfiguration;
import com.github.tofpu.speedbridge2.configuration.database.DatabaseDriverType;
import com.github.tofpu.speedbridge2.configuration.database.driver.H2DriverConfiguration;
import com.github.tofpu.speedbridge2.configuration.database.driver.MySQLDriverConfiguration;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;

public class DatabaseMapper {
    public static DriverOptions map(final DatabaseConfiguration databaseConfiguration) {
        DatabaseDriverType driverType = databaseConfiguration.currentDriverType();
        switch (driverType) {
            case H2:
                H2DriverConfiguration h2 = databaseConfiguration.h2();
                return DriverOptions.createH2(h2.connectionType());
            case MYSQL:
                MySQLDriverConfiguration mysql = databaseConfiguration.mysql();
                return DriverOptions.createMySQL(mysql.host(), mysql.database(), mysql.username(), mysql.password());
            default:
                throw new IllegalStateException("Unknown driver type: " + driverType);
        }
    }
}
