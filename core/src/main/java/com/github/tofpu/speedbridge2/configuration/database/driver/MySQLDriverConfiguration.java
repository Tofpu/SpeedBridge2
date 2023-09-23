package com.github.tofpu.speedbridge2.configuration.database.driver;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class MySQLDriverConfiguration extends DriverConfiguration {
    private String host = "localhost:3306";
    private String database = "speedbridge2";
    private String username = "root";
    private String password = "root";

    public MySQLDriverConfiguration() {
    }

    public MySQLDriverConfiguration(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.username = user;
        this.password = password;
    }

    public String host() {
        return host;
    }

    public String database() {
        return database;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
