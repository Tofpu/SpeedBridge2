package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.executable.Executable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandResolver<T extends CommandDetail> {

    private final RegisteredCommandRegistry<T> commandRegistry;

    public CommandResolver(RegisteredCommandRegistry<T> commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    private static String[] alignArguments(String[] original, String target) {
        String[] result = original;
        for (int i = 0; i < original.length; i++) {
            if (target.equals(original[i])) {
                result = Arrays.copyOfRange(original, i + 1, original.length);
                break;
            }
        }
        return result;
    }

    public ResolvedCommand resolve(String input) {
        final String[] arguments = input.split(" ");

        final String initialCommandName = arguments[0];
        final CommandDetail rootCommand = this.commandRegistry.get(initialCommandName);
        if (rootCommand == null) {
            throw new IllegalArgumentException("Unknown command: " + input);
        }

        final String[] argsWithoutRoot = Arrays.copyOfRange(arguments, 1, arguments.length);
        final CommandDetail latestCommand = recursiveCommand(rootCommand, argsWithoutRoot);

        // aligns the leftover arguments since we had taken command into consideration
        final String[] argsWithoutCommand = alignArguments(argsWithoutRoot, latestCommand.name());
        System.out.println("Found command: " + latestCommand.name());
        System.out.println("Leftover arguments: " + Arrays.toString(argsWithoutCommand) + "("
            + argsWithoutCommand.length + ")");

        if (argsWithoutCommand.length == 0 || latestCommand.subcommands().isEmpty()) {
//            latestCommand.executeDefault(argsWithoutCommand);
            return new ResolvedCommand(latestCommand, argsWithoutCommand);
        }

        final SubCommandDetail subCommandInfo = recursiveSubCommand(
            new ArrayList<>(latestCommand.subcommands()), 0, argsWithoutCommand);
        final String[] cleanArguments;
        if (subCommandInfo == null) {
            cleanArguments = new String[0];
        } else {
            String[] split = subCommandInfo.name().split(" ");
            cleanArguments = alignArguments(argsWithoutCommand, split[split.length - 1]);
        }

        if (subCommandInfo == null) {
//            latestCommand.executeDefault(argsWithoutCommand);
            return new ResolvedCommand(latestCommand, argsWithoutCommand);
        }

        System.out.println("subcommand: " + subCommandInfo.name());
        System.out.println("arguments: " + Arrays.toString(cleanArguments));
//        subCommandInfo.execute((Object[]) cleanArguments);
        return new ResolvedCommand(subCommandInfo, cleanArguments);
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
     * Attempts to find the closest sub-command match by recursively removing
     * <code>argument</code>'s right-hand-side element until a match/none is found.
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

    static class ResolvedCommand {

        private final Executable executable;
        private final Object[] arguments;

        public ResolvedCommand(Executable executable, Object[] arguments) {
            this.executable = executable;
            this.arguments = arguments;
        }

        public Executable executable() {
            return executable;
        }

        public Object[] arguments() {
            return arguments;
        }
    }
}
