package io.tofpu.speedbridge2.command.condition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.process.CommandCondition;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class AbstractCommandConditionWrapper implements CommandCondition {
    private final Class<? extends Annotation> type;

    public AbstractCommandConditionWrapper(final Class<? extends Annotation> type,
            final LampConditionRegistry registry) {
        this.type = type;
        registry.register(type, this);
    }

    public void register(final CommandHandler handler) {
        handler.registerCondition(this);
    }

    @Override
    public final void test(
            @NotNull final CommandActor actor,
            @NotNull final ExecutableCommand command,
            @NotNull @Unmodifiable final List<String> arguments) {
        if (command.hasAnnotation(type)) {
            return;
        }
        execute(actor, command, arguments);
    }

    abstract void execute(@NotNull CommandActor actor,
            @NotNull ExecutableCommand command,
            @NotNull @Unmodifiable List<String> arguments);

    public Class<? extends Annotation> getType() {
        return type;
    }
}
