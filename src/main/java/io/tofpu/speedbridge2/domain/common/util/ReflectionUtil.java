package io.tofpu.speedbridge2.domain.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class ReflectionUtil {
    public static Collection<String> toString(final Class<?> clazz) {
        final List<String> strings = new ArrayList<>();

        for (final Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(IgnoreMessage.class)) {
                continue;
            }

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            try {
                final Object object = field.get(null);

                if (!(object instanceof String)) {
                    continue;
                }
                final String string = (String) object;

                System.out.println("object: " + string);
                strings.add(field.getName() + ": " + string);
            } catch (IllegalAccessException ignored) {}
        }

        return Collections.unmodifiableList(strings);
    }
}
