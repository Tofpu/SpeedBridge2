package io.tofpu.speedbridge2.domain.common.wrapper;

import io.tofpu.speedbridge2.domain.common.PluginExecutor;
import io.tofpu.speedbridge2.domain.common.config.serializer.*;
import io.tofpu.speedbridge2.domain.common.umbrella.SerializableUmbrellaItem;
import io.tofpu.umbrella.domain.item.action.AbstractItemAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class ConfigurateFile<T> {
    private final Plugin plugin;
    private final File file;
    private ConfigurationLoader<?> loader;
    private ConfigurationNode node;

    private Class<?> bindTo;
    private T configuration;

    public ConfigurateFile(final Plugin plugin, final File targetFile) {
        this.plugin = plugin;
        this.file = targetFile;
    }

    public ConfigurateFile(final Plugin plugin, final File file, final String name) {
        this(plugin, new File(file, name));
    }

    public void load(final Class<T> bindTo, final FileConfigurationType type) {
        this.bindTo = bindTo;

        final UnaryOperator<ConfigurationOptions> configurationOptions = options -> options.shouldCopyDefaults(true)
                .serializers(builder -> builder.register(Location.class, LocationSerializer.INSTANCE)
                        .register(Material.class, MaterialSerializer.INSTANCE)
                        .register(SerializableUmbrellaItem.class, UmbrellaItemSerializer.INSTANCE)
                        .register(ItemStack.class, ItemStackSerializer.INSTANCE)
                        .register(ItemMeta.class, ItemMetaSerializer.INSTANCE)
                        .register(AbstractItemAction.class,
                                AbstractItemActionSerializer.INSTANCE));

        switch (type) {
            case YAML:
                this.loader = YamlConfigurationLoader.builder()
                        .path(file.toPath())
                        .nodeStyle(NodeStyle.BLOCK)
                        .defaultOptions(configurationOptions)
                        .build();
                break;
            case HOCON:
                this.loader = HoconConfigurationLoader.builder()
                        .path(file.toPath())
                        .defaultOptions(configurationOptions)
                        .build();
                break;
        }

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
            this.configuration = node.get(bindTo);
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
                this.configuration = (T) node.get(bindTo);
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

    public CompletableFuture<Void> update() {
        return PluginExecutor.runAsync(() -> {
            try {
                node.set(bindTo, configuration);
                loader.save(node);
            } catch (ConfigurateException e) {
                e.printStackTrace();
            }
        });
    }

    public T getConfiguration() {
        return configuration;
    }
}
