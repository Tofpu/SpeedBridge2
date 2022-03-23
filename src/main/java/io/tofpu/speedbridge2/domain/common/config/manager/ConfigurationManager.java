package io.tofpu.speedbridge2.domain.common.config.manager;

import io.tofpu.speedbridge2.domain.common.config.ItemConfiguration;
import io.tofpu.speedbridge2.domain.common.config.PluginConfiguration;
import io.tofpu.speedbridge2.domain.common.config.category.*;
import io.tofpu.speedbridge2.domain.common.wrapper.ConfigurateFile;
import io.tofpu.speedbridge2.domain.common.wrapper.FileConfigurationType;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

public final class ConfigurationManager {
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();

    private ConfigurateFile<PluginConfiguration> pluginConfiguration;
    private ConfigurateFile<ItemConfiguration> itemConfiguration;

    private ConfigurationManager() {
        // preventing direct initialization of ConfigurationManager
    }

    public void load(final Plugin plugin) {
        this.pluginConfiguration = new ConfigurateFile<>(plugin, plugin.getDataFolder(), "config.conf");
        this.itemConfiguration = new ConfigurateFile<>(plugin, plugin.getDataFolder(), "items.conf");

        this.pluginConfiguration.load(PluginConfiguration.class, FileConfigurationType.HOCON);
        this.itemConfiguration.load(ItemConfiguration.class, FileConfigurationType.YAML);
    }

    public CompletableFuture<Void> reload() {
        return pluginConfiguration.reload();
    }

    public GeneralCategory getGeneralCategory() {
        return getPluginConfiguration().getGeneralCategory();
    }

    public PluginConfiguration getPluginConfiguration() {
        return pluginConfiguration.getConfiguration();
    }

    public ItemConfiguration getConfiguration() {
        return itemConfiguration.getConfiguration();
    }

    public LeaderboardCategory getLeaderboardCategory() {
        return getPluginConfiguration().getLeaderboardCategory();
    }

    public BlockMenuCategory getBlockMenuCategory() {
        return getPluginConfiguration().getBlockMenuCategory();
    }

    public LobbyCategory getLobbyCategory() {
        return getPluginConfiguration().getLobbyCategory();
    }

    public GameCategory getGameCategory() {
        return getConfiguration().getGameCategory();
    }

    public CompletableFuture<Void> update() {
        return pluginConfiguration.update();
    }
}
