package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.command.CommandContainerDetail;
import com.github.tofpu.speedbridge2.command.SubCommandDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandContainer implements CommandContainerDetail {
    private final String name;
    private final List<? extends SubCommandDetail> subcommands;
    private final DefaultCommand defaultCommand;
    private final Map<String, CommandContainer> nestedCommands = new HashMap<>();

    public CommandContainer(String name, List<? extends SubCommandDetail> subcommands, Object object,
                            DefaultCommand defaultCommand, List<CommandContainer> nestedCommands) {
        this.name = name;
        this.subcommands = subcommands;
        this.defaultCommand = defaultCommand;

        nestedCommands.forEach(
                commandContainer -> this.nestedCommands.put(commandContainer.name(), commandContainer));
    }

    @Override
    public List<? extends SubCommandDetail> subcommands() {
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
