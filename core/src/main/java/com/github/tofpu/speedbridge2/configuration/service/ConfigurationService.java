package com.github.tofpu.speedbridge2.configuration.service;

import com.github.tofpu.speedbridge2.configuration.MyConfiguration;
import com.github.tofpu.speedbridge2.service.LoadableService;
import io.github.tofpu.dynamicconfiguration.Configuration;
import io.github.tofpu.dynamicconfiguration.service.DynamicConfigHandler;

import java.io.File;

public class ConfigurationService implements LoadableService {
    private final ConfigurateHandler configurateHandler;
    private final DynamicConfigHandler dynamicConfigHandler;

    public ConfigurationService(File pluginDirectory) {
        configurateHandler = new ConfigurateHandler(pluginDirectory, PluginConfigTypes.CONFIG.identifier());
        dynamicConfigHandler = new DynamicConfigHandler(pluginDirectory);
    }

    @Override
    public void load() {
        configurateHandler.load();
        dynamicConfigHandler.load(PluginConfigTypes.values());
    }

    @Override
    public void unload() {
        dynamicConfigHandler.unload();
    }

    public Configuration on(final PluginConfigTypes type) {
        return dynamicConfigHandler.on(type);
    }

    public MyConfiguration config() {
        return configurateHandler.config();
    }
}