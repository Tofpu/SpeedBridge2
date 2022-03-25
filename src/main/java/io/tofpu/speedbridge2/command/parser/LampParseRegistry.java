package io.tofpu.speedbridge2.command.parser;

import io.tofpu.dynamicclass.meta.AutoRegister;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AutoRegister
public final class LampParseRegistry {
    private final Map<Class<?>, AbstractLampParser<?>> parserMap = new HashMap<>();

    public void register(final Class<?> type, final AbstractLampParser<?> parser) {
        this.parserMap.put(type, parser);
    }

    public Collection<AbstractLampParser<?>> values() {
        return Collections.unmodifiableCollection(parserMap.values());
    }
}
