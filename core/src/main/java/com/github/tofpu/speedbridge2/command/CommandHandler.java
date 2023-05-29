package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.internal.MethodCommand;
import com.github.tofpu.speedbridge2.command.maker.CommandMaker;
import com.github.tofpu.speedbridge2.command.maker.MethodCommandMaker;

public class CommandHandler {
    private final RegisteredCommandRegistry<MethodCommand> commandRegistry;
    private final CommandExecutor<MethodCommand> commandExecutor;
    private final CommandMaker<MethodCommand> commandMaker;

    public CommandHandler() {
        this.commandRegistry = new RegisteredCommandRegistry<>();
        this.commandExecutor = new CommandExecutor<>(commandRegistry);
        this.commandMaker = new MethodCommandMaker();
    }

    public void register(Object object) {
        MethodCommand command = this.commandMaker.create(object);
        this.commandRegistry.register(command.name(), command);
    }

    public void invoke(String command) {
        this.commandExecutor.invoke(command);
    }
}