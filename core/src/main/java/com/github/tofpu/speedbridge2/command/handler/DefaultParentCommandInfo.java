package com.github.tofpu.speedbridge2.command.handler;

import com.github.tofpu.speedbridge2.command.Command;
import com.github.tofpu.speedbridge2.util.ReflectionUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.SneakyThrows;

public class DefaultParentCommandInfo extends ParentCommandInfo {

    private final Object object;
    private final Method defaultCommand;
    private final Map<String, DefaultParentCommandInfo> nestedCommands = new HashMap<>();

    public DefaultParentCommandInfo(String name, Object object, Method defaultCommand,
        Set<? extends SubCommandInfo> subcommands) {
        super(name, subcommands);
        this.object = object;
        this.defaultCommand = defaultCommand;

        registerNestedCommands(object);
    }

    private void registerNestedCommands(Object object) {
        Class<?> commandClass = object.getClass();
        Arrays.stream(commandClass.getDeclaredFields())
            .filter(field -> field.getType().isAnnotationPresent(
                Command.class))
            .forEach(field -> {
                System.out.println("Found field: " + field.getName());
                Object fieldValue = ReflectionUtil.get(field, object);
                System.out.println("fieldValue: " + fieldValue.getClass().getName());

                DefaultParentCommandInfo nestedCommand = CommandHandler.createCommand(
                    fieldValue);
                this.nestedCommands.put(nestedCommand.name(), nestedCommand);
            });
    }

    @SneakyThrows
    @Override
    public void defaultInvoke() {
        int parameterCount = defaultCommand.getParameterCount();
        if (parameterCount == 0) {
            defaultInvoke(new String[]{});
            return;
        }

        Object[] args = new Object[parameterCount];
        for (int i = 0; i < defaultCommand.getParameterTypes().length; i++) {
            Object value = null;
            if (Integer.class.isAssignableFrom(defaultCommand.getParameterTypes()[i])) {
                value = -1;
            }
            args[i] = value;
        }
        defaultInvoke(args);
    }

    @Override
    public void defaultInvoke(Object[] arguments) {
        if (defaultCommand == null) {
            return;
        }
        ReflectionUtil.invoke(object, defaultCommand, arguments);
    }

    @Override
    public ParentCommandInfo find(String nestedCommand) {
        return this.nestedCommands.get(nestedCommand);
    }
}
