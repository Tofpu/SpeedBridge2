package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.command.CommandDetail;
import com.github.tofpu.speedbridge2.command.CommandHandler;
import com.github.tofpu.speedbridge2.command.SubCommandDetail;
import com.github.tofpu.speedbridge2.command.annontation.Command;
import com.github.tofpu.speedbridge2.util.ReflectionUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodCommand extends AbstractCommandDetail {

    private final Object object;
    private final Method defaultCommand;
    private final Map<String, MethodCommand> nestedCommands = new HashMap<>();

    public MethodCommand(String name, List<? extends SubCommandDetail> subcommands, Object object,
        Method defaultCommand, List<MethodCommand> nestedCommands) {
        super(name, subcommands);
        this.object = object;
        this.defaultCommand = defaultCommand;

        nestedCommands.forEach(methodCommand -> this.nestedCommands.put(methodCommand.name(), methodCommand));
    }

    @Override
    public void executeDefault(Object... args) {
        if (defaultCommand == null) {
            return;
        }

        if (defaultCommand.getParameterCount() > args.length) {
            args = fillMissingParameters(args);
        }

        ReflectionUtil.invoke(object, defaultCommand, args);
    }

    private Object[] fillMissingParameters(Object[] args) {
        return Arrays.copyOfRange(args, 0, this.defaultCommand.getParameterCount());
    }

    @Override
    public CommandDetail findNested(String commandName) {
        return this.nestedCommands.get(commandName);
    }
}
