package com.github.tofpu.speedbridge2.command.executable;

import java.util.Arrays;

public class MethodExecutable implements Executable {

    private final Object owner;
    private final MethodWrapper methodWrapper;

    public MethodExecutable(Object owner, MethodWrapper methodWrapper) {
        this.owner = owner;
        this.methodWrapper = methodWrapper;
    }

    @Override
    public void invoke(Object[] arguments) {
        int parameterCount = methodWrapper.parameterCount();
        if (parameterCount > arguments.length) {
            arguments = fillMissingParameters(arguments, parameterCount);
        }

        methodWrapper.invoke(owner, arguments);
    }

    @Override
    public ExecutableParameter executableParameter() {
        return methodWrapper;
    }

    private Object[] fillMissingParameters(Object[] args, int parameterCount) {
        return Arrays.copyOfRange(args, 0, parameterCount);
    }
}
