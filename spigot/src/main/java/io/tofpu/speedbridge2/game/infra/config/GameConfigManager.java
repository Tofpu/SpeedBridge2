package io.tofpu.speedbridge2.game.infra.config;

import io.tofpu.speedbridge2.util.config.ConfigManager;
import java.io.File;
import java.nio.file.Path;

public class GameConfigManager {
    private final File configFolder;
    private ConfigManager<GameConfiguration> configManager;

    public GameConfigManager(File configFolder) {
        this.configFolder = configFolder;
    }

    public void load() {
        Path path = configFolder.toPath();
        this.configManager = ConfigManager.create(path, "game.yml", GameConfiguration.class);
        this.configManager.reloadConfig();
    }

    public GameConfiguration getConfigData() {
        if (configManager == null) {
            throw new IllegalStateException("Game config manager has not been loaded yet");
        }
        return this.configManager.getConfigData();
    }
}
