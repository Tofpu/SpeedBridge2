package com.github.tofpu.speedbridge2.command.argument;

import com.github.tofpu.speedbridge2.command.annontation.Join;
import com.github.tofpu.speedbridge2.command.executable.ExecutableParameter;
import com.github.tofpu.speedbridge2.command.executable.ExecutableParameter.Parameter;
import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ArgumentResolver {

    public Object[] resolve(ExecutableParameter executableParameter, Object[] args) {
        if (args.length != 0 && executableParameter.parameterCount() != 0 && contain(
            executableParameter, Join.class)) {
            System.out.println(
                "Found @Join annotation, joining given args: " + Arrays.toString(args));
            args = new Object[]{args};
            System.out.println("Result: " + Arrays.toString(args));
        }
        return args;
    }

    private boolean contain(ExecutableParameter executableParameter,
        Class<? extends Annotation> clazz) {
        for (Parameter parameter : executableParameter.parameters()) {
            if (parameter.isAnnotationPresent(clazz)) {
                return true;
            }
        }
        return false;
    }
}
