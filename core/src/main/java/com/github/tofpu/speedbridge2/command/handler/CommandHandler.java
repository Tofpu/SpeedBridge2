package com.github.tofpu.speedbridge2.command.handler;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.command.Command;
import com.github.tofpu.speedbridge2.command.Default;
import com.github.tofpu.speedbridge2.command.Subcommand;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandHandler {
    private final Map<String, ParentCommandInfo> commandMap = new HashMap<>();

    public static DefaultParentCommandInfo createCommand(Object object) {
        Class<?> commandClass = object.getClass();
        requireState(commandClass.isAnnotationPresent(Command.class), "Class %s must have @Command annotation", commandClass.getSimpleName());

        String commandName = commandClass.getAnnotation(Command.class).name();
        Set<Method> subcommands = subcommands(commandClass);
        Method defaultCommand = defaultCommand(commandClass);

        Set<DefaultSubCommandInfo> subcommandsWithInfo = subcommands.stream().map(method -> {
            String subcommandName = method.getAnnotation(Subcommand.class).name();
            return new DefaultSubCommandInfo(subcommandName, object, method);
        }).collect(Collectors.toSet());
        return new DefaultParentCommandInfo(commandName, object,
            defaultCommand, subcommandsWithInfo);
    }

    public void register(Object object) {
        DefaultParentCommandInfo command = createCommand(object);
        this.commandMap.put(command.name(), command);
    }

    @Nullable
    private static Method defaultCommand(Class<?> commandClass) {
        List<Method> defaultCommands = Arrays.stream(commandClass.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Default.class))
            .collect(Collectors.toList());
        requireState(defaultCommands.size() == 0 || defaultCommands.size() == 1, "Class %s must only contain single default subcommand!", commandClass.getSimpleName());

        Method defaultCommand = null;
        if (!defaultCommands.isEmpty()) {
            defaultCommand = defaultCommands.get(0);
        }
        return defaultCommand;
    }

    public void invoke(String command) {
        final String rootCommand = command.split(" ")[0];
        ParentCommandInfo parentCommandInfo = this.commandMap.get(rootCommand);
        if (parentCommandInfo == null) {
            throw new IllegalArgumentException("Unknown command: " + command);
        }

        String[] arguments;

        int index = 0;
        while (true) {
            int indexOffset = index + 1;
            arguments = new String[command.split(" ").length - indexOffset];
            System.arraycopy(command.split(" "), indexOffset, arguments, 0, arguments.length);
            System.out.println("After arguments: " + Arrays.toString(arguments));

            if (index >= arguments.length) break;
            ParentCommandInfo nestedCommand = parentCommandInfo.find(arguments[index++]);

            if (nestedCommand == null) break;
            parentCommandInfo = nestedCommand;
        }

        System.out.println("Found command: " + parentCommandInfo.name());
        System.out.println("Leftover arguments: " + Arrays.toString(arguments));

        if (arguments.length == 0) {
            parentCommandInfo.defaultInvoke();
            return;
        }

        String[] subcommandsArguments = new String[arguments.length - 1];
        String subcommandTarget = arguments[0];
        SubCommandInfo subCommandInfo = null;
        for (SubCommandInfo subcommand : parentCommandInfo.subcommands()) {
            if (subcommandTarget.equalsIgnoreCase(subcommand.name())) {
                subCommandInfo = subcommand;
                System.arraycopy(arguments, 1, subcommandsArguments, 0, subcommandsArguments.length);
                break;
            }
        }

        if (subCommandInfo != null) {
            System.out.println("subcommand: " + subCommandInfo.name());
            System.out.println("arguments: " + Arrays.toString(subcommandsArguments));
            subCommandInfo.invoke(subcommandsArguments);
            return;
        }
        parentCommandInfo.defaultInvoke(arguments);
    }

    @NotNull
    private static Set<Method> subcommands(Class<?> commandClass) {
        return Arrays.stream(commandClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Subcommand.class))
            .collect(Collectors.toSet());
    }
}
