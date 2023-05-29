package com.github.tofpu.speedbridge2.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandExecutor<T extends CommandDetail> {
    private final RegisteredCommandRegistry<T> commandRegistry;

    public CommandExecutor(RegisteredCommandRegistry<T> commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    public void invoke(String command) {
        final String[] commandArgs = command.split(" ");
        final String rootName = commandArgs[0];

        final CommandDetail rootCommand = this.commandRegistry.get(rootName);
        if (rootCommand == null) {
            throw new IllegalArgumentException("Unknown command: " + command);
        }

        final String[] argsWithoutRoot = Arrays.copyOfRange(commandArgs, 1, commandArgs.length);
        final CommandDetail latestCommand = recursiveCommand(rootCommand, argsWithoutRoot);

        // aligns the leftover arguments since we had taken command into consideration
        final String[] argsWithoutCommand = alignArguments(argsWithoutRoot, latestCommand.name());

        System.out.println("Found command: " + latestCommand.name());
        System.out.println("Leftover arguments: " + Arrays.toString(argsWithoutCommand));

        if (argsWithoutCommand.length == 0 || latestCommand.subcommands().isEmpty()) {
            latestCommand.executeDefault();
            return;
        }

        final SubCommandDetail subCommandInfo = recursiveSubCommand(
            new ArrayList<>(latestCommand.subcommands()), 0, argsWithoutCommand);
        final String[] cleanArguments = subCommandInfo == null ? new String[0]
            : alignArguments(argsWithoutCommand, subCommandInfo.name());

        if (subCommandInfo == null) {
            latestCommand.executeDefault((Object[]) argsWithoutCommand);
            return;
        }

        System.out.println("subcommand: " + subCommandInfo.name());
        System.out.println("arguments: " + Arrays.toString(cleanArguments));
        subCommandInfo.execute((Object[]) cleanArguments);
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

    private CommandDetail recursiveCommand(CommandDetail command, String[] arguments) {
        System.out.println(Arrays.toString(arguments));
        if (arguments.length == 0) {
            return command;
        }

        CommandDetail attempt = command.findNested(arguments[0]);
        if (attempt == null) {
            return command;
        }
        return recursiveCommand(attempt, Arrays.copyOfRange(arguments, 1, arguments.length));
    }

    /**
     * Attempts to find the closest sub-command match by recursively removing <code>argument</code>'s
     * right-hand-side element until a match/none is found.
     *
     * <p></p>
     * <pre>
     *     String[] input = {"root", "print", "set, "hello"};
     *     List<SubCommandInfo> commands = List.of("root", "print", "set");
     *
     *     // checks whether there is a subcommand with the name: "root print set hello"
     *     // there is none, removes right-hand-side of argument (which is "hello")
     *     // checks whether there is a subcommand with the name: "root print set"
     *     // found!
     * </pre>
     *
     * @param commands  the subcommands
     * @param index     the index (should be 0 by default)
     * @param arguments the user inputs (be sure to exclude used arguments)
     * @return closest sub-command match.
     */
    private SubCommandDetail recursiveSubCommand(List<SubCommandDetail> commands, int index,
        String[] arguments) {
        // return once we reach the end of subcommands
        if (index >= commands.size()) {
            return null;
        }

        final SubCommandDetail subCommandInfo = commands.get(index);
        String[] args = arguments;
        while (args.length != 0) {
            // return our match if we find one
            if (String.join(" ", args).equals(subCommandInfo.name())) {
                return subCommandInfo;
            }
            // bummer, we have to remove argument's right side chunk.
            args = Arrays.copyOfRange(arguments, 0, args.length - 1);
        }
        return recursiveSubCommand(commands, index + 1, arguments);
    }
}
