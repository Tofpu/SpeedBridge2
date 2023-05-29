package com.github.tofpu.speedbridge2.command;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.command.annontation.Command;
import com.github.tofpu.speedbridge2.command.annontation.Default;
import com.github.tofpu.speedbridge2.command.annontation.Subcommand;
import com.github.tofpu.speedbridge2.command.internal.MethodCommand;
import com.github.tofpu.speedbridge2.command.internal.MethodSubCommand;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandHandler {
    private final RegisteredCommandRegistry<MethodCommand> commandRegistry;
    private final CommandExecutor<MethodCommand> commandExecutor;

    public CommandHandler() {
        this.commandRegistry = new RegisteredCommandRegistry<>();
        this.commandExecutor = new CommandExecutor<>(commandRegistry);
    }

    public static MethodCommand createCommand(Object object) {
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
        return new MethodCommand(commandName, object,
            defaultCommand, subcommandsWithInfo);
    }

    @Nullable
    private static Method defaultCommand(Class<?> commandClass) {
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
    private static Set<Method> subcommands(Class<?> commandClass) {
        return Arrays.stream(commandClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Subcommand.class))
            .collect(Collectors.toSet());
    }

    public void register(Object object) {
        MethodCommand command = createCommand(object);
        this.commandRegistry.register(command.name(), command);
    }

    public void invoke(String command) {
        this.commandExecutor.invoke(command);
    }
}