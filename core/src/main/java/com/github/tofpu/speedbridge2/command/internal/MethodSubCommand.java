package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.command.SubCommandDetail;
import com.github.tofpu.speedbridge2.command.executable.MethodExecutable;
import java.lang.reflect.Method;

public class MethodSubCommand extends MethodExecutable implements SubCommandDetail {

    private final String name;

    public MethodSubCommand(String name, Object object, Method method) {
        super(object, method);
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }
}
