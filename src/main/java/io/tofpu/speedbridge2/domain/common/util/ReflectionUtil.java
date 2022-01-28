package io.tofpu.speedbridge2.domain.common.util;

import java.lang.reflect.Field;
import java.util.*;

public final class ReflectionUtil {
    public static Collection<String> toString(final Map<String, Field> fieldMap, final Class<?> clazz) {
        final List<String> strings = new ArrayList<>();

        for (final Field field : fieldMap.values()) {
            try {
                final Object object = field.get(null);

                System.out.println("object: " + object);
                strings.add(field.getName() + ": " + object);
            } catch (IllegalAccessException ignored) {}
        }

        return Collections.unmodifiableList(strings);
    }
}
