package com.github.tofpu.speedbridge2.command.internal.maker;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.command.SubCommandDetail;
import com.github.tofpu.speedbridge2.command.annontation.Command;
import com.github.tofpu.speedbridge2.command.annontation.Default;
import com.github.tofpu.speedbridge2.command.annontation.Subcommand;
import com.github.tofpu.speedbridge2.command.internal.MethodCommand;
import com.github.tofpu.speedbridge2.command.internal.MethodSubCommand;
import com.github.tofpu.speedbridge2.util.ReflectionUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MethodCommandMaker extends CommandMaker<MethodCommand> {

    @Override
    public MethodCommand create(Object object) {
        Class<?> commandClass = object.getClass();
        requireState(commandClass.isAnnotationPresent(Command.class),
            "Class %s must have @Command annotation", commandClass.getSimpleName());

        String commandName = commandClass.getAnnotation(Command.class).name();
        Set<Method> subcommands = subcommands(commandClass);
        Method defaultCommand = defaultCommand(commandClass);

        List<SubCommandDetail> subcommandsWithInfo = subcommands.stream().map(method -> {
            String subcommandName = method.getAnnotation(Subcommand.class).name();
            return new MethodSubCommand(subcommandName, object, method);
        }).collect(Collectors.toList());

        return new MethodCommand(commandName, subcommandsWithInfo, object, defaultCommand,
            nestedCommands(object));
    }

    private List<MethodCommand> nestedCommands(Object object) {
        Class<?> commandClass = object.getClass();
        return Arrays.stream(commandClass.getDeclaredFields())
            .filter(field -> field.getType().isAnnotationPresent(
                Command.class))
            .map(field -> {
                System.out.println("Found field: " + field.getName());
                Object fieldValue = ReflectionUtil.get(field, object);
                System.out.println("fieldValue: " + fieldValue.getClass().getName());

                return create(fieldValue);
            }).collect(Collectors.toList());
    }

    @Nullable
    private Method defaultCommand(Class<?> commandClass) {
        List<Method> defaultCommands = Arrays.stream(commandClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Default.class))
            .collect(Collectors.toList());
        requireState(defaultCommands.size() == 0 || defaultCommands.size() == 1,
            "Class %s must only contain single default subcommand!", commandClass.getSimpleName());

        Method defaultCommand = null;
        if (!defaultCommands.isEmpty()) {
            defaultCommand = defaultCommands.get(0);
        }
        return defaultCommand;
    }

    @NotNull
    private Set<Method> subcommands(Class<?> commandClass) {
        return Arrays.stream(commandClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Subcommand.class))
            .collect(Collectors.toSet());
    }
}
