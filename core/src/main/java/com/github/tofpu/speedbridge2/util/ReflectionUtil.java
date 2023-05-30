package com.github.tofpu.speedbridge2.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

public class ReflectionUtil {

    public static void invoke(final Object object, final Method method, final Object... args) {
//        boolean accessible = true;

        access(method, () -> {
            try {
                method.invoke(object, args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

//        try {
//            invoke(method, object, args);
//        } catch (Exception e) {
//            if (!(e instanceof IllegalAccessException)) {
//                throw e;
//            }
//
//            accessible = false;
//            method.setAccessible(true);
//            ReflectionUtils.invoke(method, object, args);
//        }

//        if (!accessible) {
//            method.setAccessible(false);
//        }
    }

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
}
