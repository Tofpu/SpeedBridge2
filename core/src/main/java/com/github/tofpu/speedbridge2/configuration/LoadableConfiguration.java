package com.github.tofpu.speedbridge2.configuration;

import java.io.File;

public interface LoadableConfiguration extends Configuration {
    void load(File fromFile);
    void save(File toFile);
}
