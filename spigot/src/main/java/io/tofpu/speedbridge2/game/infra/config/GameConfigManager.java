package io.tofpu.speedbridge2.game.infra.config;

import io.tofpu.speedbridge2.game.domain.GameFeedback;
import io.tofpu.speedbridge2.game.domain.GameFeedbackRegistry;
import io.tofpu.speedbridge2.game.infra.config.experience.GamePlayerExperienceConfiguration;
import io.tofpu.speedbridge2.util.config.ConfigManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public class GameConfigManager {
    private final File configFolder;
    private ConfigManager<GameConfiguration> configManager;

    public GameConfigManager(File configFolder) {
        this.configFolder = configFolder;
    }

    public GameConfiguration load() {
        Path path = configFolder.toPath();
        this.configManager = ConfigManager.create(path, "game.yml", GameConfiguration.class);
        this.configManager.reloadConfig();
        return getConfigData();
    }

    public GameFeedbackRegistry loadFeedbackRegistry() {
        GameConfiguration configData = load();
        GameFeedbackRegistry registry = new GameFeedbackRegistry();
        GamePlayerExperienceConfiguration experience = configData.experience();
        registry.register(GameFeedbackRegistry.Type.SCORE, getFeedback(experience.score()));
        registry.register(GameFeedbackRegistry.Type.RESET, getFeedback(experience.reset()));
        registry.register(GameFeedbackRegistry.Type.BEATEN_SCORE, getFeedback(experience.beatenScore()));
        return registry;
    }

    private static @NotNull GameFeedback getFeedback(GamePlayerExperienceConfiguration.GameOptions gameOptions) {
        return new GameFeedback(
                gameOptions.sound().toSound(),
                gameOptions.title().toTitle(),
                gameOptions.commands(),
                gameOptions.messages()
        );
    }

    public GameConfiguration getConfigData() {
        if (configManager == null) {
            throw new IllegalStateException("Game config manager has not been loaded yet");
        }
        return this.configManager.getConfigData();
    }
}
