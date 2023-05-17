package com.github.tofpu.speedbridge2.util;

import lombok.SneakyThrows;

public class ReflectionUtil {
    @SneakyThrows
    public static void setStaticField(final Class<?> clazz, final String field, final Object value) {
        clazz.getDeclaredField(field).set(null, value);
    }
}
