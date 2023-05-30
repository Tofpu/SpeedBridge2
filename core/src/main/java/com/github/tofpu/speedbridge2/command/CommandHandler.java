package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.CommandExecutor.ExecutableCommand;
import com.github.tofpu.speedbridge2.command.CommandResolver.ResolvedCommand;
import com.github.tofpu.speedbridge2.command.argument.ArgumentResolver;
import com.github.tofpu.speedbridge2.command.executable.Executable;
import com.github.tofpu.speedbridge2.command.internal.MethodCommand;
import com.github.tofpu.speedbridge2.command.maker.CommandMaker;
import com.github.tofpu.speedbridge2.command.maker.MethodCommandMaker;

public class CommandHandler {

    private final RegisteredCommandRegistry<MethodCommand> registry;
    private final CommandResolver<MethodCommand> resolver;
    private final CommandMaker<MethodCommand> factory;
    private final ArgumentResolver argumentResolver;
    private final CommandExecutor executor;

    public CommandHandler() {
        this.registry = new RegisteredCommandRegistry<>();
        this.resolver = new CommandResolver<>(registry);
        this.factory = new MethodCommandMaker();
        this.executor = new CommandExecutor();
        this.argumentResolver = new ArgumentResolver();
    }

    public void register(Object object) {
        MethodCommand command = this.factory.create(object);
        this.registry.register(command.name(), command);
    }

    public void invoke(String command) {
        ResolvedCommand resolvedCommand = this.resolver.resolve(command);

        Executable executable = resolvedCommand.executable();
        Object[] arguments = argumentResolver.resolve(executable.executableParameter(),
            resolvedCommand.arguments());
        ExecutableCommand executableCommand = new ExecutableCommand(executable, arguments);
        this.executor.execute(executableCommand);
    }
}