package io.tofpu.speedbridge2.domain.common.config.manager;

import com.google.common.io.Files;
import io.tofpu.speedbridge2.domain.common.config.ItemConfiguration;
import io.tofpu.speedbridge2.domain.common.config.PluginConfiguration;
import io.tofpu.speedbridge2.domain.common.config.category.*;
import io.tofpu.speedbridge2.domain.common.wrapper.ConfigurateFile;
import io.tofpu.speedbridge2.domain.common.wrapper.FileConfigurationType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class ConfigurationManager {
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();

    private ConfigurateFile<PluginConfiguration> pluginConfiguration;
    private ConfigurateFile<ItemConfiguration> itemConfiguration;

    private ConfigurationManager() {
        // preventing direct initialization of ConfigurationManager
    }

    public void load(final Plugin plugin) {
        final File directory = new File(plugin.getDataFolder(), "configurations");
        if (!directory.exists()) {
            directory.mkdir();
        }

        // if the server used a version anything lower
        // than 1.0.8, then the migration process will be executed
        oldConfigMigration(plugin.getDataFolder(), directory);

        this.pluginConfiguration = new ConfigurateFile<>(plugin, directory, "config.conf");
        this.itemConfiguration = new ConfigurateFile<>(plugin, directory, "items.yml");

        this.pluginConfiguration.load(PluginConfiguration.class, FileConfigurationType.HOCON);
        this.itemConfiguration.load(ItemConfiguration.class, FileConfigurationType.YAML);
    }

    public void oldConfigMigration(final File fromDirectory, final File toDirectory) {
        final File oldConfig = new File(fromDirectory, "config.conf");
        if (oldConfig.exists()) {
            try {
                final File to = new File(toDirectory, "config.conf");
                Files.move(oldConfig, to);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public CompletableFuture<Void> reload() {
        final CompletableFuture<?>[] futures = new CompletableFuture[2];
        futures[0] = pluginConfiguration.reload();
        futures[1] = itemConfiguration.reload();

        return CompletableFuture.allOf(futures);
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
