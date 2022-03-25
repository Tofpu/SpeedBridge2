package io.tofpu.speedbridge2.command.parser;

import revxrsal.commands.CommandHandler;
import revxrsal.commands.process.ValueResolver;

public abstract class AbstractLampParser<T> {
    private final Class<T> type;

    public AbstractLampParser(final Class<T> type, final LampParseRegistry registry) {
        this.type = type;
        registry.register(type, this);
    }

    public void register(final CommandHandler commandHandler) {
        commandHandler.registerValueResolver(type, this::parse);
    }

    abstract T parse(ValueResolver.ValueResolverContext context);

    public Class<T> getType() {
        return type;
    }
}
