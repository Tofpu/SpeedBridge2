package com.github.tofpu.speedbridge2.command.handler;

import static com.github.tofpu.speedbridge2.util.ProgramCorrectness.requireState;

import com.github.tofpu.speedbridge2.command.Command;
import com.github.tofpu.speedbridge2.command.Default;
import com.github.tofpu.speedbridge2.command.Subcommand;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
        final String[] commandArgs = command.split(" ");
        final String rootName = commandArgs[0];

        final ParentCommandInfo rootCommand = this.commandMap.get(rootName);
        if (rootCommand == null) {
            throw new IllegalArgumentException("Unknown command: " + command);
        }

        final String[] argsWithoutRoot = Arrays.copyOfRange(commandArgs, 1, commandArgs.length);
        final ParentCommandInfo latestCommand = recursiveCommand(rootCommand, argsWithoutRoot);

        // aligns the leftover arguments since we had taken command into consideration
        final String[] argsWithoutCommand = alignArguments(argsWithoutRoot, latestCommand.name());

        System.out.println("Found command: " + latestCommand.name());
        System.out.println("Leftover arguments: " + Arrays.toString(argsWithoutCommand));

        if (argsWithoutCommand.length == 0 || latestCommand.subcommands().isEmpty()) {
            latestCommand.defaultInvoke();
            return;
        }

        final SubCommandInfo subCommandInfo = recursiveSubCommand(
            new ArrayList<>(latestCommand.subcommands()), 0, argsWithoutCommand);
        final String[] cleanArguments = subCommandInfo == null ? new String[0]
            : alignArguments(argsWithoutCommand, subCommandInfo.name());

        if (subCommandInfo == null) {
            latestCommand.defaultInvoke(argsWithoutCommand);
            return;
        }

        System.out.println("subcommand: " + subCommandInfo.name());
        System.out.println("arguments: " + Arrays.toString(cleanArguments));
        subCommandInfo.invoke(cleanArguments);
    }

    private static String[] alignArguments(String[] arguments, String subCommandInfo) {
        String[] result = new String[0];
        for (int i = 0; i < arguments.length; i++) {
            if (subCommandInfo.equals(arguments[i])) {
                result = Arrays.copyOfRange(arguments, i + 1, arguments.length);
                break;
            }
        }
        return result;
    }

    private ParentCommandInfo recursiveCommand(ParentCommandInfo command, String[] arguments) {
        System.out.println(Arrays.toString(arguments));
        if (arguments.length == 0) {
            return command;
        }

        ParentCommandInfo attempt = command.find(arguments[0]);
        if (attempt == null) {
            return command;
        }
        return recursiveCommand(attempt, Arrays.copyOfRange(arguments, 1, arguments.length));
    }

    private SubCommandInfo recursiveSubCommand(List<SubCommandInfo> commands, int index, String[] arguments) {
        if (index >= commands.size()) {
            return null;
        }
        SubCommandInfo subCommandInfo = commands.get(index);
        String[] args = arguments;
        while (args.length != 0) {
            if (String.join(" ", args).equals(subCommandInfo.name())) {
                return subCommandInfo;
            }
            args = Arrays.copyOfRange(arguments, 0, args.length - 1);
        }
        return recursiveSubCommand(commands, index + 1, arguments);
    }

    @NotNull
    private static Set<Method> subcommands(Class<?> commandClass) {
        return Arrays.stream(commandClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Subcommand.class))
            .collect(Collectors.toSet());
    }
}