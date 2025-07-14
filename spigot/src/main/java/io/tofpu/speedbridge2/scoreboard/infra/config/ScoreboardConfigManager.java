package io.tofpu.speedbridge2.scoreboard.infra.config;

import io.tofpu.speedbridge2.util.config.ConfigManager;

import java.io.File;

public class ScoreboardConfigManager {
    public ScoreboardConfiguration loadConfiguration(File scoreboardFile) {
        ConfigManager<ScoreboardConfiguration> configManager = ConfigManager.create(scoreboardFile.getParentFile().toPath(), scoreboardFile.getName(), ScoreboardConfiguration.class);
        configManager.reloadConfig();
        return configManager.getConfigData();
    }
}
