package com.github.tofpu.speedbridge2.command.internal.executable;

interface ExecutableDetail {

    MethodWrapper methodWrapper();
}

public interface Executable extends ExecutableDetail {

    void invoke(Object[] arguments);
}
