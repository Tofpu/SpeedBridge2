package com.github.tofpu.speedbridge2.configuration.service;

public enum ConfigType {
    CONFIG("config"),
    MESSAGE("message");


    private final String identifier;

    ConfigType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
