package com.github.tofpu.speedbridge2.command.internal.executable;

import java.lang.annotation.Annotation;

public interface MethodWrapper {

    int parameterCount();

    Parameter[] parameters();

    interface Parameter {

        Class<?> type();

        boolean isAnnotationPresent(Class<? extends Annotation> clazz);
    }
}
