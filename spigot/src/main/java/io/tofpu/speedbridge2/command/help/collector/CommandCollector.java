package io.tofpu.speedbridge2.command.help.collector;

import io.tofpu.speedbridge2.command.help.ParentCommand;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.core.CommandPath;

import java.util.*;
import java.util.function.Consumer;

public class CommandCollector {

    public Map<String, ParentCommand> collectCommands(CommandHandler handler) {
        Map<String, TreeCommandNode> commandNodeMap = collectAsNode(handler);
        Map<String, ParentCommand> parentCommandMap = new HashMap<>();

        commandNodeMap.forEach((parent, treeCommandNode) -> {
            Iterator<TreeCommandNode> iterator = treeCommandNode.childrenIterator();
            List<ExecutableCommand> subCommands = new LinkedList<>();
            iterate(parent, commandInfo -> subCommands.add(commandInfo.executableCommand()), iterator);

            parentCommandMap.put(parent, new ParentCommand(parent, treeCommandNode.data().aliases(), subCommands));
        });

        return parentCommandMap;
    }

    static void iterate(String rootPath, Consumer<CommandInfo> pathConsumer, Iterator<TreeCommandNode> iterator) {
        while (iterator.hasNext()) {
            TreeCommandNode next = iterator.next();
            CommandInfo commandEntry = next.data();

            String subcommandName = commandEntry.name();
            String path = rootPath + " " + subcommandName;

            if (next.children().isEmpty()) {
                pathConsumer.accept(commandEntry);
                continue;
            }

            // traverse to children's commands
            Iterator<TreeCommandNode> childrenIterator = next.childrenIterator();
            iterate(path, pathConsumer, childrenIterator);
        }
    }

    @NotNull Map<String, TreeCommandNode> collectAsNode(CommandHandler handler) {
        Set<Integer> duplicateId = new TreeSet<>();

        // sub command to parent mapping
        Map<Integer, TreeCommandNode> subCommandToParentMapping = new HashMap<>();
        // this is where the parent commands are stored
        Map<String, TreeCommandNode> parentCommands = new LinkedHashMap<>();

        // this is where the commands are sorted based on size; low to high
        Map<CommandPath, ExecutableCommand> sortedCommandsMap = getAndSortCommands(handler);

        for (Map.Entry<CommandPath, ExecutableCommand> entry : sortedCommandsMap.entrySet()) {
            CommandPath commandPath = entry.getKey();
            ExecutableCommand executableCommand = entry.getValue();

            int commandId = executableCommand.getId();
            duplicateId.add(commandId);

            String parent = commandPath.getParent();
            TreeCommandNode parentTreeNode = parentCommands.get(parent);

            // lets us find the 'original' command name if the subcommands were previously processed
            if (parentTreeNode == null) {
                parentTreeNode = subCommandToParentMapping.get(commandId);
            }

            if (parentTreeNode == null) {
                parentTreeNode = new TreeCommandNode(new CommandInfo(parent));
                parentCommands.put(parent, parentTreeNode);
                subCommandToParentMapping.put(commandId, parentTreeNode);
            } else if (duplicateId.contains(commandId)) {
                parentTreeNode.data().addAlias(parent);
            }

            TreeCommandNode subcommandNode = parentTreeNode;
            for (String subPath : commandPath.getSubcommandPath()) {
                subcommandNode = subcommandNode.path(subPath, new CommandInfo(parent));
            }
            subcommandNode.data(new CommandInfo(commandPath.getName(), executableCommand));
        }
        return parentCommands;
    }

    static @NotNull Map<CommandPath, ExecutableCommand> getAndSortCommands(CommandHandler handler) {
        Map<CommandPath, ExecutableCommand> sortedCommandsMap = new LinkedHashMap<>();

        handler.getCommands().keySet().
                stream().sorted(Comparator.comparingInt(CommandPath::size))
                .forEachOrdered(path -> sortedCommandsMap.put(path, handler.getCommand(path)));
        return sortedCommandsMap;
    }
}
