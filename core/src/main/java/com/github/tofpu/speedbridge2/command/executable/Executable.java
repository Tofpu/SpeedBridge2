package com.github.tofpu.speedbridge2.command.executable;

interface ExecutableDetail {

    ExecutableParameter executableParameter();
}

public interface Executable extends ExecutableDetail {

    void invoke(Object[] arguments);
}
