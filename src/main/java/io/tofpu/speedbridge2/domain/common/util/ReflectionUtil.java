package io.tofpu.speedbridge2.domain.common.util;

import io.tofpu.speedbridge2.domain.common.Message;

import java.lang.reflect.Field;
import java.util.*;

public final class ReflectionUtil {
    public static Collection<String> toString(final Map<String, Field> fieldMap) {
        final List<String> strings = new ArrayList<>();

        for (final Field field : fieldMap.values()) {
            try {
                final String message = String.valueOf(field.get(Message.INSTANCE))
                        .replace("\n", "\\n");

                BridgeUtil.debug("object: " + message);
                strings.add(field.getName() + ": " + message);
            } catch (IllegalAccessException ignored) {
                // do nothing
            }
        }

        return Collections.unmodifiableList(strings);
    }
}
