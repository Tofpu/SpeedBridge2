package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.command.SubCommand;
import com.github.tofpu.speedbridge2.command.internal.executable.MethodExecutable;
import com.github.tofpu.speedbridge2.command.internal.executable.MethodWrapperImpl;

import java.lang.reflect.Method;

public class MethodSubCommand extends MethodExecutable implements SubCommand {

    private final String name;

    public MethodSubCommand(String name, Object object, Method method) {
        super(object, new MethodWrapperImpl(method));
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }
}
