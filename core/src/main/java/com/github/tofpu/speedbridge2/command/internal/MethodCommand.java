package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.command.CommandDetail;
import com.github.tofpu.speedbridge2.command.SubCommandDetail;
import com.github.tofpu.speedbridge2.command.executable.MethodExecutable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodCommand extends MethodExecutable implements CommandDetail {

    private final String name;
    private final List<? extends SubCommandDetail> subcommands;
    private final Map<String, MethodCommand> nestedCommands = new HashMap<>();

    public MethodCommand(String name, List<? extends SubCommandDetail> subcommands, Object object,
        Method defaultCommand, List<MethodCommand> nestedCommands) {
        super(object, defaultCommand);
        this.name = name;
        this.subcommands = subcommands;

        nestedCommands.forEach(
            methodCommand -> this.nestedCommands.put(methodCommand.name(), methodCommand));
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
    public CommandDetail findNested(String commandName) {
        return this.nestedCommands.get(commandName);
    }
}
