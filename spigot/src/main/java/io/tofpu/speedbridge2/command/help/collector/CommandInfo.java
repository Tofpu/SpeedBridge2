package io.tofpu.speedbridge2.command.help.collector;

import revxrsal.commands.command.ExecutableCommand;

import java.util.Set;
import java.util.TreeSet;

public class CommandInfo {
    private final String name;
    private final Set<String> aliases = new TreeSet<>();
    private ExecutableCommand executableCommand = null;

    public CommandInfo(String name) {
        this.name = name;
    }

    public CommandInfo(String name, ExecutableCommand executableCommand) {
        this.name = name;
        this.executableCommand = executableCommand;
    }

    public void addAlias(String name) {
        aliases.add(name);
    }

    public void executableCommand(ExecutableCommand executableCommand) {
        this.executableCommand = executableCommand;
    }

    public String name() {
        return name;
    }

    public ExecutableCommand executableCommand() {
        return executableCommand;
    }

    public Set<String> aliases() {
        return aliases;
    }
}
