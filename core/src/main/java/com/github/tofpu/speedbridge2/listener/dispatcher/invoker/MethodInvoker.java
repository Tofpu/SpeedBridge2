package com.github.tofpu.speedbridge2.listener.dispatcher.invoker;

import com.github.tofpu.speedbridge2.listener.Event;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.reflections.ReflectionUtils;

public class MethodInvoker implements ListenerInvoker {

    private final Object object;
    private final Method method;

    public MethodInvoker(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    @Override
    public void invoke(Event event) {
        final boolean accessible =
            !Modifier.isProtected(method.getModifiers()) && !Modifier.isPrivate(
                method.getModifiers());

        if (!accessible) {
            method.setAccessible(true);
        }

        try {
            ReflectionUtils.invoke(method, object, event);
        } catch (Exception e) {
            if (!accessible) {
                method.setAccessible(false);
            }
            throw new IllegalStateException(e);
        }

        if (!accessible) {
            method.setAccessible(true);
        }
    }

    @Override
    public Class<?> type() {
        return method.getParameterTypes()[0];
    }

    @Override
    public String name() {
        return object.getClass().getSimpleName();
    }
}
