package io.tofpu.speedbridge2.score.infra.config;

import io.tofpu.speedbridge2.score.format.rounding.domain.RoundingSettings;
import io.tofpu.speedbridge2.score.infra.config.format.ScoreFormatConfiguration;
import io.tofpu.speedbridge2.score.domain.RegistrySettings;
import io.tofpu.speedbridge2.util.config.ConfigManager;

import java.io.File;
import java.nio.file.Path;

public class ScoreConfigurationLoader {
    private final File configFolder;
    private ConfigManager<ScoreConfiguration> configManager;

    public ScoreConfigurationLoader(File configFolder) {
        this.configFolder = configFolder;
    }

    public void load() {
        Path path = configFolder.toPath();
        this.configManager = ConfigManager.create(path, "score.yml", ScoreConfiguration.class);
        this.configManager.reloadConfig();
    }

    public RegistrySettings registrySettings() {
        return new RegistrySettings(data().registry().entriesLimit());
    }

    public RoundingSettings formatSettings() {
        ScoreFormatConfiguration.Rounding format = data().format().rounding();
        return new RoundingSettings(
                format.mode(),
                format.precision(),
                format.decimalPlaces()
        );
    }

    ScoreConfiguration data() {
        if (configManager == null) {
            throw new IllegalStateException("Game config manager has not been loaded yet");
        }
        return this.configManager.getConfigData();
    }
}
