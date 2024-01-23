package io.tofpu.speedbridge2.command.parser;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.command.NameAndUUID;
import revxrsal.commands.process.ValueResolver;

import java.util.UUID;

@AutoRegister
public class NameAndUUIDParser extends AbstractLampParser<NameAndUUID> {
    private final AbstractLampParser<UUID> uuidParser;

    public NameAndUUIDParser(LampParseRegistry registry, PlayerUUIDParser uuidParser) {
        super(NameAndUUID.class, registry);
        this.uuidParser = uuidParser;
    }

    @Override
    NameAndUUID parse(ValueResolver.ValueResolverContext context) {
        String name = context.arguments().peekFirst();
        UUID parsedUUID = uuidParser.parse(context);
        return new NameAndUUID(name, parsedUUID);
    }
}
