package com.github.tofpu.speedbridge2.database;

import com.github.tofpu.speedbridge2.MagicValue;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import com.github.tofpu.speedbridge2.database.factory.DatabaseFactory;
import com.github.tofpu.speedbridge2.database.factory.session.SessionFactoryMaker;

public class DatabaseBuilder {
    private DatabaseFactory<?> factory;

    DatabaseBuilder() {
        // prevents direct initialization
    }

    public DatabaseBuilder factory(DatabaseFactory<?> factory) {
        this.factory = factory;
        return this;
    }

    public Database build(DriverOptions driverOptions) {
        return factory.create(
                SessionFactoryMaker.create(MagicValue.APPLICATION_PACKAGE_NAME, driverOptions));
    }
}
