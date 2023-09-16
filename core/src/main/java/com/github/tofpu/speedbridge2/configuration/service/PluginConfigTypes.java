package com.github.tofpu.speedbridge2.configuration.service;

import io.github.tofpu.dynamicconfiguration.service.ConfigType;

public enum PluginConfigTypes implements ConfigType {
    CONFIG("config"),
    MESSAGE("message");


    private final String identifier;

    PluginConfigTypes(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String identifier() {
        return identifier;
    }
}
