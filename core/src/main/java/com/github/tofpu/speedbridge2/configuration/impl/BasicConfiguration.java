package com.github.tofpu.speedbridge2.configuration.impl;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class BasicConfiguration {

    private final Map<String, Object> objectMap = new HashMap<>();
    private final Yaml yaml;

    public BasicConfiguration() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yaml = new Yaml(dumperOptions);
    }

    @SneakyThrows
    public void load(final File fromFile) {
        requireState(!fromFile.isDirectory(), "This %s must be a file, not a directory!",
            fromFile.getPath());
        if (!fromFile.exists()) {
            File parentFile = fromFile.getParentFile();
            if (!parentFile.exists()) {
                Files.createDirectories(parentFile.toPath());
            }
            Files.createFile(fromFile.toPath());
        }

        Map<String, Object> loadedMap = this.yaml.load(new FileInputStream(fromFile));
        this.objectMap.putAll(loadedMap);
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
