package io.tofpu.speedbridge2.domain.common.config.manager;

import io.tofpu.speedbridge2.domain.common.config.PluginConfiguration;
import io.tofpu.speedbridge2.domain.common.config.category.*;
import io.tofpu.speedbridge2.domain.common.wrapper.ConfigurateFile;
import io.tofpu.speedbridge2.domain.common.wrapper.FileConfigurationType;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

public final class ConfigurationManager {
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();

    private ConfigurateFile<PluginConfiguration> configuration;

    private ConfigurationManager() {}

    public void load(final Plugin plugin) {
        this.configuration = new ConfigurateFile<>(plugin,
                plugin.getDataFolder(), "config.conf");

        this.configuration.load(PluginConfiguration.class, FileConfigurationType.HOCON);
    }

    public CompletableFuture<Void> reload() {
        return configuration.reload();
    }

    public GeneralCategory getGeneralCategory() {
        return getConfiguration().getGeneralCategory();
    }

    public LeaderboardCategory getLeaderboardCategory() {
        return getConfiguration().getLeaderboardCategory();
    }

    public BlockMenuCategory getBlockMenuCategory() {
        return getConfiguration().getBlockMenuCategory();
    }

    public LobbyCategory getLobbyCategory() {
        return getConfiguration().getLobbyCategory();
    }

    public GameCategory getGameCategory() {
        return getConfiguration().getGameCategory();
    }

    public PluginConfiguration getConfiguration() {
        return configuration.getConfiguration();
    }

    public CompletableFuture<Void> update() {
        return configuration.update();
    }
}
