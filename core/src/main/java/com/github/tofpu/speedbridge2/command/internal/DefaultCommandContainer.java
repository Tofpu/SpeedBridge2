package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.command.CommandContainer;
import com.github.tofpu.speedbridge2.command.SubCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCommandContainer implements CommandContainer {
    private final String name;
    private final List<? extends SubCommand> subcommands;
    private final DefaultCommand defaultCommand;
    private final Map<String, CommandContainer> nestedCommands = new HashMap<>();

    public DefaultCommandContainer(String name, List<? extends SubCommand> subcommands,
                                   DefaultCommand defaultCommand, List<CommandContainer> nestedCommands) {
        this.name = name;
        this.subcommands = subcommands;
        this.defaultCommand = defaultCommand;

        nestedCommands.forEach(
                commandContainer -> this.nestedCommands.put(commandContainer.name(), commandContainer));
    }

    @Override
    public List<? extends SubCommand> subcommands() {
        return this.subcommands;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public CommandContainer findNested(String commandName) {
        return this.nestedCommands.get(commandName);
    }

    @Override
    public DefaultCommand defaultCommand() {
        return defaultCommand;
    }
}
