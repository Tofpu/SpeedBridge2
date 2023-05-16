package com.github.tofpu.speedbridge2.configuration.impl;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class BasicConfiguration {

    protected final Map<String, Object> objectMap;
    private final Yaml yaml;

    public BasicConfiguration(Map<String, Object> objectMap) {
        this.objectMap = objectMap;

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yaml = new Yaml(dumperOptions);
    }

    public BasicConfiguration() {
        this(new LinkedHashMap<>());
    }

    @SneakyThrows
    public void load(final File fromFile) {
        requireState(!fromFile.isDirectory(), "This %s must be a file, not a directory!",
            fromFile.getPath());
        if (!fromFile.exists()) {
            createFile(fromFile);
        }

        if (!fromFile.exists()) {
            throw new IllegalStateException("Unable to create file: " + fromFile.getName());
        }

        Map<String, Object> loadedMap = this.yaml.load(new FileInputStream(fromFile));
        if (loadedMap != null && loadedMap.size() != 0) {
            this.objectMap.putAll(loadedMap);
        }
    }

    private static void createFile(File fromFile) throws IOException {
        File parentFile = fromFile.getParentFile();
        if (!parentFile.exists()) {
            Files.createDirectories(parentFile.toPath());
        }
        Files.createFile(fromFile.toPath());
    }

    public void set(final String key, final Object obj) {
        this.objectMap.put(key, obj);
    }

    public Object get(final String key, final Object defaultValue) {
        return this.objectMap.getOrDefault(key, defaultValue);
    }

    @SneakyThrows
    public void save(final File toFile) {
        yaml.dump(objectMap, new PrintWriter(toFile));
    }
}
