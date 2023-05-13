package com.github.tofpu.speedbridge2.configuration.impl;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

@SuppressWarnings("unused")
public class AdvancedConfiguration extends BasicConfiguration {

    public void setAs(final String key, final Object value) {
        set(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAs(final Class<T> type, final String key, final T defaultValue) {
        Object value = get(key, defaultValue);
        if (value == null) {
            return null;
        }
        requireState(type.isInstance(value), "Value %s is not assignable to %s!",
            value.getClass().getSimpleName(), type.getSimpleName());
        return (T) value;
    }

    public void setString(final String key, final String value) {
        setAs(key, value);
    }

    public void setInt(final String key, final int integer) {
        setAs(key, integer);
    }

    public String getString(final String key, final String defaultValue) {
        return getAs(String.class, key, defaultValue);
    }

    public int getInt(final String key, final int defaultValue) {
        return getAs(Integer.class, key, defaultValue);
    }
}
