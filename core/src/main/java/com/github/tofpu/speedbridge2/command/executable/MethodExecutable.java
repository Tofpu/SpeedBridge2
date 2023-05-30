package com.github.tofpu.speedbridge2.command.executable;

import com.github.tofpu.speedbridge2.util.ReflectionUtil;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodExecutable implements Executable {

    private final Object owner;
    private final Method method;
    private final MethodExecutableParameter executableParameter;

    public MethodExecutable(Object owner, Method method) {
        this.owner = owner;
        this.method = method;
        this.executableParameter = new MethodExecutableParameter(method);
    }

    @Override
    public void invoke(Object[] arguments) {
        int parameterCount = method.getParameterCount();
        if (parameterCount > arguments.length) {
            arguments = fillMissingParameters(arguments, parameterCount);
        }

        ReflectionUtil.invoke(owner, method, arguments);
    }

    @Override
    public ExecutableParameter executableParameter() {
        return executableParameter;
    }

    private Object[] fillMissingParameters(Object[] args, int parameterCount) {
        return Arrays.copyOfRange(args, 0, parameterCount);
    }
}
