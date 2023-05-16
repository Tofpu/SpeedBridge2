package com.github.tofpu.speedbridge2.configuration;

import com.github.tofpu.speedbridge2.configuration.impl.node.NodeConfiguration;
import java.util.Map;

public interface Configuration {
    NodeConfiguration path(String key);
    NodeConfiguration previous();

    Configuration set(String key, Object value);

    <T> T getAs(Class<T> type, String key, T defaultValue);

    Configuration setString(String key, String value);

    Configuration setInt(String key, int value);

    String getString(String key, String defaultValue);

    int getInt(String key, int defaultValue);

    Map<String, Object> data();
}
