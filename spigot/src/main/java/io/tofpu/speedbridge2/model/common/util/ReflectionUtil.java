package io.tofpu.speedbridge2.model.common.util;

import io.tofpu.speedbridge2.model.common.Message;

import java.lang.reflect.Field;
import java.util.*;

public final class ReflectionUtil {
    /**
     * This function takes a map of field names to fields and returns a list of strings.
     *
     * @param fieldMap A map of field names to Field objects.
     * @return A list of strings.
     */
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
