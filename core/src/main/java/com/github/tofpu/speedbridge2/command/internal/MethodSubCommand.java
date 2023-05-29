package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.util.ReflectionUtil;
import java.lang.reflect.Method;

public class MethodSubCommand extends AbstractSubCommand {
    private final Object object;
    private final Method method;

    public MethodSubCommand(String name, Object object, Method method) {
        super(name);
        this.object = object;
        this.method = method;
    }

    @Override
    public void execute(Object... args) {
        System.out.println("Invoking method: " + method.getName());
        ReflectionUtil.invoke(object, method, args);
    }
}
