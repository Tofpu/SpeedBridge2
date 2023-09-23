package com.github.tofpu.speedbridge2.database.driver;

public interface DriverOptions {

    String connectionUrl();

    String dialect();

    String driverClass();

    String username();
    String password();
}
