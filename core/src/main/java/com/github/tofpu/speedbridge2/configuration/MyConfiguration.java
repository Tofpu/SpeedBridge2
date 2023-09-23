package com.github.tofpu.speedbridge2.configuration;

import com.github.tofpu.speedbridge2.configuration.database.DatabaseConfiguration;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class MyConfiguration {
    private final DatabaseConfiguration database = new DatabaseConfiguration();

    public DatabaseConfiguration database() {
        return database;
    }
}
