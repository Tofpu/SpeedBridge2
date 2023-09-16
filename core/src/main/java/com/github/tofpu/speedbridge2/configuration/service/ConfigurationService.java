package com.github.tofpu.speedbridge2.configuration.service;

import com.github.tofpu.speedbridge2.service.LoadableService;
import io.github.tofpu.dynamicconfiguration.Configuration;
import io.github.tofpu.dynamicconfiguration.service.DynamicConfigHandler;

import java.io.File;

public class ConfigurationService implements LoadableService {
    private final DynamicConfigHandler dynamicConfigHandler;

    public ConfigurationService(File pluginDirectory) {
        dynamicConfigHandler = new DynamicConfigHandler(pluginDirectory);
    }

    @Override
    public void load() {
        dynamicConfigHandler.load(PluginConfigTypes.values());
    }

    @Override
    public void unload() {
        dynamicConfigHandler.unload();
    }

    public Configuration on(final PluginConfigTypes type) {
        return dynamicConfigHandler.on(type);
    }
}