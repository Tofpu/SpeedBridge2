package io.tofpu.speedbridge2.command.condition;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.command.condition.annotation.RestrictConsole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.exception.CommandErrorException;

import java.util.List;

@AutoRegister
public final class RestrictConsoleCondition extends AbstractCommandConditionWrapper {
    public RestrictConsoleCondition(final LampConditionRegistry registry) {
        super(RestrictConsole.class, registry);
    }

    @Override
    void execute(
            @NotNull final CommandActor actor,
            @NotNull final ExecutableCommand command,
            @NotNull @Unmodifiable final List<String> arguments) {
        final BukkitCommandActor commandActor = (BukkitCommandActor) actor;

        if (commandActor.isConsole()) {
            throw new CommandErrorException("Console is not allowed to execute this command.");
        }
    }
}
