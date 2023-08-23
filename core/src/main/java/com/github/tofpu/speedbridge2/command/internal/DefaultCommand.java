package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.command.internal.executable.Executable;
import com.github.tofpu.speedbridge2.command.internal.executable.MethodWrapper;
import com.github.tofpu.speedbridge2.command.internal.executable.MethodExecutable;
import com.github.tofpu.speedbridge2.command.internal.executable.MethodWrapperImpl;

import java.lang.reflect.Method;

public class DefaultCommand implements Executable {
    private final MethodExecutable executable;

    public DefaultCommand(Object owner, Method method) {
        if (method != null) {
            this.executable = new MethodExecutable(owner, new MethodWrapperImpl(method));
        } else {
            executable = null;
        }
    }

    public void invoke(Object[] arguments) {
        requireCommandToBeAvailable();
        assert executable != null;
        executable.invoke(arguments);
    }

    private void requireCommandToBeAvailable() {
        if (executable == null) throw new NoCommandFallbackException();
    }

    @Override
    public MethodWrapper methodWrapper() {
        requireCommandToBeAvailable();
        assert executable != null;
        return executable.methodWrapper();
    }
}
