package io.tofpu.speedbridge2.command.context;

import revxrsal.commands.CommandHandler;
import revxrsal.commands.process.ContextResolver;

public abstract class AbstractLampContext<T> implements ContextResolver<T> {
    private final Class<T> type;

    public AbstractLampContext(final Class<T> type, final LampContextRegistry registry) {
        this.type = type;
        registry.register(type, this);
    }

    public void register(final CommandHandler commandHandler) {
        commandHandler.registerContextResolver(type, this);
    }

    public Class<T> getType() {
        return type;
    }
}
