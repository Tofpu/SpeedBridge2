package com.github.tofpu.speedbridge2.configuration.impl;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.configuration.Configuration;
import com.github.tofpu.speedbridge2.configuration.LoadableConfiguration;
import com.github.tofpu.speedbridge2.configuration.impl.node.NodeConfiguration;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class AdvancedConfiguration implements LoadableConfiguration {

    private final BasicConfiguration delegate;

    public AdvancedConfiguration() {
        this(new HashMap<>(), new BasicConfiguration());
    }

    public AdvancedConfiguration(final Map<String, Object> objectMap,
        BasicConfiguration delegate) {
        this.delegate = delegate;
    }

    @Override
    public NodeConfiguration path(final String key) {
        return new NodeConfiguration(this, new String[]{key});
    }

    @Override
    public NodeConfiguration previous() {
        throw new RuntimeException("Root section has no previous path.");
    }

    @Override
    public Configuration set(final String key, final Object value) {
        delegate.set(key, value);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAs(final Class<T> type, final String key, final T defaultValue) {
        Object value = delegate.get(key, defaultValue);
        if (value == null) {
            return null;
        }
        requireState(type.isInstance(value), "Value %s is not assignable to %s!",
            value.getClass().getSimpleName(), type.getSimpleName());
        return (T) value;
    }

    @Override
    public Configuration setString(final String key, final String value) {
        return this.set(key, value);
    }

    @Override
    public Configuration setInt(final String key, final int value) {
        return this.set(key, value);
    }

    @Override
    public String getString(final String key, final String defaultValue) {
        return getAs(String.class, key, defaultValue);
    }

    @Override
    public int getInt(final String key, final int defaultValue) {
        return getAs(Integer.class, key, defaultValue);
    }

    @Override
    public Map<String, Object> data() {
        return Collections.unmodifiableMap(delegate.objectMap);
    }

    @Override
    public void load(File fromFile) {
        delegate.load(fromFile);
    }

    @Override
    public void save(File toFile) {
        delegate.save(toFile);
    }
}
