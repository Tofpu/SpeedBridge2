package com.github.tofpu.speedbridge2.configuration.impl.node;

import com.github.tofpu.speedbridge2.configuration.Configuration;
import java.util.Map;

public class NodeConfiguration implements Configuration {
    private final Configuration delegate;
    private final Node node;

    public NodeConfiguration(Configuration delegate, String[] paths) {
        this.delegate = delegate;
        this.node = new Node(delegate, paths);
    }

    @Override
    public NodeConfiguration path(String key) {
        return node.path(key);
    }

    @Override
    public Configuration set(String key, Object value) {
        node.set(key, value);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAs(Class<T> type, String key, T defaultValue) {
        return (T) node.get(key, defaultValue);
    }

    @Override
    public Configuration setString(String key, String value) {
        set(key, value);
        return this;
    }

    @Override
    public Configuration setInt(String key, int value) {
        set(key, value);
        return this;
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getAs(String.class, key, defaultValue);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return getAs(Integer.class, key, defaultValue);
    }

    @Override
    public Map<String, Object> data() {
        return delegate.data();
    }

    @Override
    public NodeConfiguration previous() {
        return node.previous();
    }
}
