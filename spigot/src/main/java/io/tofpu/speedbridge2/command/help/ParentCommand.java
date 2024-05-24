package io.tofpu.speedbridge2.command.help;

import revxrsal.commands.command.ExecutableCommand;

import java.util.*;

public class ParentCommand {
    private final String name;
    private final Set<String> aliases;
    private final List<ExecutableCommand> commands;

    public ParentCommand(String name, Collection<String> aliases, Collection<ExecutableCommand> commands) {
        this.name = name;
        this.aliases = new LinkedHashSet<>(aliases);
        this.commands = new LinkedList<>(commands);
    }

    public String name() {
        return name;
    }

    public Collection<String> aliases() {
        return Collections.unmodifiableSet(aliases);
    }

    public Collection<ExecutableCommand> commands() {
        return Collections.unmodifiableList(commands);
    }
}
