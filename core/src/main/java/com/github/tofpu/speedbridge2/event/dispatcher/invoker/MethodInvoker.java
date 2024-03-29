package com.github.tofpu.speedbridge2.event.dispatcher.invoker;

import com.github.tofpu.speedbridge2.event.Event;
import org.reflections.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvoker implements EventInvoker {

    private final Object object;
    private final Method method;

    public MethodInvoker(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    @Override
    public void invoke(Event event) {
        boolean accessible = true;

        try {
            invoke(method, object, event);
        } catch (Exception e) {
            if (!(e instanceof IllegalAccessException)) {
                Throwable cause = e.getCause();
                if (e.getCause() != null && e instanceof InvocationTargetException) {
                    cause = e.getCause();
                }
                throw new IllegalStateException("Ran into an error while calling method " + method.getName() + "()", cause);
            }

            accessible = false;
            try {
                method.setAccessible(true);
                ReflectionUtils.invoke(method, object, event);
            } catch (Exception e2) {
                throw new IllegalStateException(
                    "Unable to access listener method " + method.getName() + " of "
                        + object.getClass().getSimpleName(), e2);
            }
        }

        if (!accessible) {
            method.setAccessible(false);
        }
    }

    private void invoke(Method method, Object obj, Object... args)
        throws InvocationTargetException, IllegalAccessException {
        method.invoke(obj, args);
    }

    @Override
    public Class<?> type() {
        return method.getParameterTypes()[0];
    }

    @Override
    public String name() {
        return object.getClass().getSimpleName();
    }

    public Object object() {
        return object;
    }

    public Method method() {
        return method;
    }
}
