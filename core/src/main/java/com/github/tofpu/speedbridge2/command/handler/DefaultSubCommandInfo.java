package com.github.tofpu.speedbridge2.command.handler;

import com.github.tofpu.speedbridge2.util.ReflectionUtil;
import java.lang.reflect.Method;

public class DefaultSubCommandInfo extends SubCommandInfo {
    private final Object object;
    private final Method method;

    protected DefaultSubCommandInfo(String name, Object object, Method method) {
        super(name);
        this.object = object;
        this.method = method;
    }

    @Override
    public void invoke(String[] arguments) {
        System.out.println("Invoking method: " + method.getName());
        ReflectionUtil.invoke(object, method, (Object[]) arguments);
    }
}
