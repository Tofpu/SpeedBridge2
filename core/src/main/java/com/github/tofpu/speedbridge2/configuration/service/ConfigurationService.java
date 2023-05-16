package com.github.tofpu.speedbridge2.configuration.service;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.configuration.Configuration;
import com.github.tofpu.speedbridge2.configuration.LoadableConfiguration;
import com.github.tofpu.speedbridge2.configuration.impl.AdvancedConfiguration;
import com.github.tofpu.speedbridge2.service.LoadableService;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationService implements LoadableService {

    private final File pluginDirectory;
    private final Map<ConfigType, LoadableConfiguration> configurationMap = new HashMap<>();

    public ConfigurationService(File pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }

    @Override
    public void load() {
        for (ConfigType value : ConfigType.values()) {
            this.configurationMap.put(value, new AdvancedConfiguration());
        }
        this.configurationMap.forEach((type, configuration) -> {
            configuration.load(new File(pluginDirectory, type.getIdentifier() + ".yml"));
        });
    }

    @Override
    public void unload() {
        this.configurationMap.forEach((type, configuration) -> {
            configuration.save(new File(pluginDirectory, type.getIdentifier() + ".yml"));
        });
    }

    public Configuration on(final ConfigType type) {
        requireState(this.configurationMap.containsKey(type), "Unknown %s configuration type",
            type.getIdentifier());
        return this.configurationMap.get(type);
    }
}