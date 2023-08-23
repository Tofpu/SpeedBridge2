package com.github.tofpu.speedbridge2.command.resolve;

import com.github.tofpu.speedbridge2.command.internal.executable.MethodWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class SenderResolver {
    private final Map<Class<?>, Function<UUID, ?>> resolveMap = new HashMap<>();

    public <T> void register(Class<T> clazz, Function<UUID, T> applyFunction) {
        this.resolveMap.put(clazz, applyFunction);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(final Class<T> clazz, UUID id) {
        Function<UUID, ?> applyFunction = this.resolveMap.get(clazz);
        if (applyFunction == null) return null;
        return (T) applyFunction.apply(id);
    }

    public Object resolve(MethodWrapper methodWrapper, UUID actorId) {
        if (methodWrapper.parameterCount() < 1) {
            return null;
        }

        MethodWrapper.Parameter firstParameter = methodWrapper.parameters()[0];
        return resolve(firstParameter.type(), actorId);
    }
}
