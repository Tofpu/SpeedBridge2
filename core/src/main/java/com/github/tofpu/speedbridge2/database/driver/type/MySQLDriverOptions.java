package com.github.tofpu.speedbridge2.database.driver.type;

import com.github.tofpu.speedbridge2.configuration.database.driver.MySQLDriverConfiguration;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;

public class MySQLDriverOptions implements DriverOptions {
    private final MySQLDriverConfiguration configuration;

    public MySQLDriverOptions(MySQLDriverConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String connectionUrl() {
        return String.format("jdbc:mysql://%s/%s", configuration.host(), configuration.database());
    }

    @Override
    public String dialect() {
        return "org.hibernate.dialect.MySQL5Dialect";
    }

    @Override
    public String driverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String username() {
        return configuration.username();
    }

    @Override
    public String password() {
        return configuration.password();
    }

}
