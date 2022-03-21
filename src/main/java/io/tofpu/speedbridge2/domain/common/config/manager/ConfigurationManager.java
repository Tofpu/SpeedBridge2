package io.tofpu.speedbridge2.domain.common.config.manager;

import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.config.PluginConfiguration;
import io.tofpu.speedbridge2.domain.common.config.category.*;
import io.tofpu.speedbridge2.domain.common.config.serializer.*;
import io.tofpu.speedbridge2.domain.common.umbrella.SerializableUmbrellaItem;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.concurrent.CompletableFuture;

public final class ConfigurationManager {
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();

    private HoconConfigurationLoader loader;
    private CommentedConfigurationNode node;
    private PluginConfiguration configuration;

    private Plugin plugin;

    private ConfigurationManager() {}

    public void load(final Plugin plugin) {
        this.plugin = plugin;

        this.loader = HoconConfigurationLoader.builder()
                .path(plugin.getDataFolder()
                        .toPath()
                        .resolve("config.conf"))
                .defaultOptions(configurationOptions -> configurationOptions.shouldCopyDefaults(true)
                        .serializers(builder -> builder.register(Location.class, LocationSerializer.INSTANCE)
                                .register(Material.class, MaterialSerializer.INSTANCE)
                                .register(SerializableUmbrellaItem.class, UmbrellaItemSerializer.INSTANCE)
                                .register(ItemStack.class, ItemStackSerializer.INSTANCE)
                                .register(ItemMeta.class, ItemMetaSerializer.INSTANCE)
                                .register(AbstractItemAction.class, AbstractItemActionSerializer.INSTANCE)))
                .build();

        try {
            this.node = loader.load();
        } catch (ConfigurateException e) {
            plugin.getLogger()
                    .warning("An error occurred while loading this configuration:" + " " +
                             e.getMessage());
            if (e.getCause() != null) {
                e.printStackTrace();
            }
            return;
        }

        try {
            this.configuration = node.get(PluginConfiguration.class);
        } catch (SerializationException e) {
            plugin.getLogger()
                    .warning("An error occurred while converting MyConfiguration: " +
                             e.getMessage());
            if (e.getCause() != null) {
                e.printStackTrace();
            }
        }

        // in-case the file doesn't exist, this will generate one for us
        this.save();
    }

    public CompletableFuture<Void> reload() {
        return CompletableFuture.runAsync(() -> {
            try {
                this.node = loader.load();
                this.configuration = node.get(PluginConfiguration.class);
            } catch (ConfigurateException e) {
                plugin.getLogger()
                        .warning("An error occurred while converting MyConfiguration: " +
                                 e.getMessage());
                if (e.getCause() != null) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void save() {
        if (this.node == null) {
            plugin.getLogger()
                    .warning("Configuration cannot be saved due to the node " +
                             "being null");
            return;
        }
        try {
            this.loader.save(node);
        } catch (ConfigurateException e) {
            plugin.getLogger()
                    .warning("An error occurred while saving this configuration: " +
                             e.getMessage());
            if (e.getCause() != null) {
                e.printStackTrace();
            }
        }
    }

    public GeneralCategory getGeneralCategory() {
        return configuration.getGeneralCategory();
    }

    public LeaderboardCategory getLeaderboardCategory() {
        return configuration.getLeaderboardCategory();
    }

    public BlockMenuCategory getBlockMenuCategory() {
        return configuration.getBlockMenuCategory();
    }

    public LobbyCategory getLobbyCategory() {
        return configuration.getLobbyCategory();
    }

    public GameCategory getGameCategory() {
        return configuration.getGameCategory();
    }

    public PluginConfiguration getConfiguration() {
        return configuration;
    }

    public CompletableFuture<Void> update() {
        return PluginExecutor.runAsync(() -> {
            try {
                node.set(PluginConfiguration.class, configuration);
                loader.save(node);
            } catch (ConfigurateException e) {
                e.printStackTrace();
            }
        });
    }
}
