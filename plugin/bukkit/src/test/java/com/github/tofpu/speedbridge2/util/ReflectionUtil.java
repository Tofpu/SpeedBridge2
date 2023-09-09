package com.github.tofpu.speedbridge2.util;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static void setStaticField(final Class<?> clazz, final String fieldName,
        final Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            boolean accessible = field.isAccessible();
            if (!accessible) {
                field.setAccessible(true);
            }
            field.set(null, value);

            // set the field back to false if it was not accessible previously
            if (!accessible) {
                field.setAccessible(false);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
