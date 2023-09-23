package com.github.tofpu.speedbridge2.configuration.database;

import com.github.tofpu.speedbridge2.configuration.database.driver.DriverConfiguration;
import com.github.tofpu.speedbridge2.configuration.database.driver.H2DriverConfiguration;
import com.github.tofpu.speedbridge2.configuration.database.driver.MySQLDriverConfiguration;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class DatabaseConfiguration {
    private DatabaseDriverType driverType = DatabaseDriverType.H2;
    private final DriverSettingsCollection driverSettings = new DriverSettingsCollection();

    public DatabaseDriverType currentDriverType() {
        return driverType;
    }

    public <T> T get(final DatabaseDriverType driverType) {
        switch (driverType) {
            case H2:
                return (T) driverSettings.h2;
            case MYSQL:
                return (T) driverSettings.mysql;
            default:
                throw new IllegalStateException("Unsupported driver type: " + driverType);
        }
    }

    @ConfigSerializable
    private static class DriverSettingsCollection {
        @Setting("MySQL")
        private final MySQLDriverConfiguration mysql = new MySQLDriverConfiguration();
        @Setting("H2")
        private final H2DriverConfiguration h2 = new H2DriverConfiguration();
    }
}
