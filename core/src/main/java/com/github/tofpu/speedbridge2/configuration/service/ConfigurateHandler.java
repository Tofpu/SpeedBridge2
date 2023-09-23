package com.github.tofpu.speedbridge2.configuration.service;

import com.github.tofpu.speedbridge2.configuration.MyConfiguration;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;

public class ConfigurateHandler {
    private final File file;
    private CommentedConfigurationNode node;
    private MyConfiguration config;
    private YamlConfigurationLoader loader;

    public ConfigurateHandler(File directory, String identifier) {
        this.file = new File(directory, identifier + ".yml");
    }

    public void load() {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        loader = YamlConfigurationLoader.builder()
                .path(file.toPath())
                .build();

        try {
            node = loader.load();
            config = node.get(MyConfiguration.class);

            save();
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            node.set(MyConfiguration.class, config());
            loader.save(node);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload() {
        load();
    }

    public MyConfiguration config() {
        return config;
    }
}
