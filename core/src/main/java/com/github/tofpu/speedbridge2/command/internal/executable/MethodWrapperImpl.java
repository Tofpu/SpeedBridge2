package com.github.tofpu.speedbridge2.command.internal.executable;

import com.github.tofpu.speedbridge2.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodWrapperImpl implements
        MethodWrapper {

    private final Method method;
    private final Parameter[] parameters;

    public MethodWrapperImpl(Method method) {
        assert method != null;
        this.method = method;
        this.parameters = Arrays.stream(method.getParameters()).map(MethodParameter::new)
            .toArray(Parameter[]::new);
    }

    public void invoke(Object owner, Object[] arguments) {
        ReflectionUtil.invoke(owner, method, arguments);
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
