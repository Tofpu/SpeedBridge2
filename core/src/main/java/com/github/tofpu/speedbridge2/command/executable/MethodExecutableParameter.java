package com.github.tofpu.speedbridge2.command.executable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodExecutableParameter implements
    ExecutableParameter {

    private final Method method;
    private final Parameter[] parameters;

    public MethodExecutableParameter(Method method) {
        this.method = method;
        this.parameters = Arrays.stream(method.getParameters()).map(MethodParameter::new)
            .toArray(Parameter[]::new);
    }

    @Override
    public int parameterCount() {
        return method.getParameterCount();
    }

    @Override
    public Parameter[] parameters() {
        return parameters;
    }

    static class MethodParameter implements Parameter {

        private final java.lang.reflect.Parameter parameter;

        MethodParameter(java.lang.reflect.Parameter parameter) {
            this.parameter = parameter;
        }

        @Override
        public Class<?> type() {
            return parameter.getType();
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> clazz) {
            return Arrays.stream(parameter.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation.annotationType().equals(clazz));
        }
    }
}
