package com.github.tofpu.speedbridge2.database.driver;

public interface DriverOptions {
    String connectionUrl(ConnectionDetails data);
    String dialect();
    String driverClass();
}
