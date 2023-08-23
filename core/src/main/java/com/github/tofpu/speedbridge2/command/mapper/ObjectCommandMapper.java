package com.github.tofpu.speedbridge2.command.mapper;

import com.github.tofpu.speedbridge2.command.CommandContainer;
import com.github.tofpu.speedbridge2.command.SubCommand;
import com.github.tofpu.speedbridge2.command.annontation.Command;
import com.github.tofpu.speedbridge2.command.annontation.Default;
import com.github.tofpu.speedbridge2.command.annontation.Subcommand;
import com.github.tofpu.speedbridge2.command.internal.DefaultCommand;
import com.github.tofpu.speedbridge2.command.internal.DefaultCommandContainer;
import com.github.tofpu.speedbridge2.command.internal.MethodSubCommand;
import com.github.tofpu.speedbridge2.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

public class ObjectCommandMapper implements CommandMapper<CommandContainer> {
    @Override
    public CommandContainer map(Object owner) {
        Class<?> commandClass = owner.getClass();
        requireState(commandClass.isAnnotationPresent(Command.class),
                "Class %s must have @Command annotation", commandClass.getSimpleName());

        DefaultCommand defaultCommand = defaultCommand(owner, commandClass);
        List<SubCommand> subcommands = subcommands(owner, commandClass);

        String commandName = commandClass.getAnnotation(Command.class).name();
        List<CommandContainer> nestedCommands = nestedCommands(owner);
        return new DefaultCommandContainer(commandName, subcommands, defaultCommand, nestedCommands);
    }

    private DefaultCommand defaultCommand(Object owner, Class<?> commandClass) {
        List<Method> defaultCommands = Arrays.stream(commandClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Default.class))
                .collect(Collectors.toList());
        requireState(defaultCommands.size() == 0 || defaultCommands.size() == 1,
                "Class %s must only contain single default subcommand!", commandClass.getSimpleName());

        Method defaultCommand = null;
        if (!defaultCommands.isEmpty()) {
            defaultCommand = defaultCommands.get(0);
        }
        return new DefaultCommand(owner, defaultCommand);
    }

    private List<SubCommand> subcommands(Object owner, Class<?> commandClass) {
        Set<Method> methodsOfSubcommands = Arrays.stream(commandClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Subcommand.class))
                .collect(Collectors.toSet());

        assert !methodsOfSubcommands.contains(null);

        return methodsOfSubcommands.stream().map(method -> {
            String subcommandName = method.getAnnotation(Subcommand.class).name();
            MethodSubCommand methodSubCommand = new MethodSubCommand(subcommandName, owner, method);
            return (SubCommand) methodSubCommand;
        }).collect(Collectors.toList());
    }

    private List<CommandContainer> nestedCommands(Object object) {
        Class<?> commandClass = object.getClass();
        return Arrays.stream(commandClass.getDeclaredFields())
                .filter(field -> field.getType().isAnnotationPresent(
                        Command.class))
                .map(field -> {
                    System.out.println("Found field: " + field.getName());
                    Object fieldValue = ReflectionUtil.get(field, object);
                    System.out.println("fieldValue: " + fieldValue.getClass().getName());

                    return map(fieldValue);
                }).collect(Collectors.toList());
    }
}
