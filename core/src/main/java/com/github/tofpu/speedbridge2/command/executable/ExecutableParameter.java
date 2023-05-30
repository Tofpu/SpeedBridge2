package com.github.tofpu.speedbridge2.command.executable;

import java.lang.annotation.Annotation;

public interface ExecutableParameter {

    int parameterCount();

    Parameter[] parameters();

    interface Parameter {

        Class<?> type();

        boolean isAnnotationPresent(Class<? extends Annotation> clazz);
    }
}
