package com.github.tofpu.speedbridge2.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ReflectionUtil {
    public static void access(AccessibleObject accessibleObject, Runnable statement) {
        boolean accessible = true;
        try {
            statement.run();
        } catch (Exception e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            if (!(cause instanceof IllegalAccessException)) {
                throw e;
            }

            accessible = false;
            accessibleObject.setAccessible(true);
            statement.run();
        }

        if (!accessible) {
            accessibleObject.setAccessible(false);
        }
    }

    public static Object get(Field field, Object object) {
        AtomicReference<Object> value = new AtomicReference<>(null);
        access(field, () -> {
            try {
                value.set(field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return value.get();
    }
    public static void set(Field field, Object object, Object value) {
        access(field, () -> {
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Set<Class<?>> getTypesAnnotatedWith(String packageName,
                                                       Class<? extends Annotation> annotation) {
        return new Reflections(packageName).getTypesAnnotatedWith(annotation);
    }
}
