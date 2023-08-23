package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.resolve.argument.ArgumentResolver;
import com.github.tofpu.speedbridge2.command.internal.executable.Executable;
import com.github.tofpu.speedbridge2.command.internal.executable.MethodWrapper;
import com.github.tofpu.speedbridge2.command.resolve.CommandResolver;
import com.github.tofpu.speedbridge2.command.resolve.ResolvedCommand;
import com.github.tofpu.speedbridge2.command.resolve.SenderResolver;

import java.util.UUID;

public class CommandInvoker<T extends CommandContainer> {
    private final CommandResolver<T> commandResolver;
    private final SenderResolver senderResolver;
    private final ArgumentResolver argumentResolver;

    public CommandInvoker(CommandResolver<T> commandResolver, SenderResolver senderResolver, ArgumentResolver argumentResolver) {
        this.commandResolver = commandResolver;
        this.senderResolver = senderResolver;
        this.argumentResolver = argumentResolver;
    }

    public void invoke(UUID actorId, String input) {
        ResolvedCommand resolvedCommand = commandResolver.resolve(input);
        Executable executable = resolvedCommand.executable();

        MethodWrapper methodWrapper = executable.methodWrapper();
        Object actor = senderResolver.resolve(methodWrapper, actorId);
        Object[] arguments = argumentResolver.resolve(methodWrapper,
                resolvedCommand.arguments(), actor);
        
        executable.invoke(arguments);
    }
}
