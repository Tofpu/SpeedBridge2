package com.github.tofpu.speedbridge2.command.sender;

import com.github.tofpu.speedbridge2.command.executable.ExecutableParameter;

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

    public Object resolve(ExecutableParameter executableParameter, UUID actorId) {
        if (executableParameter.parameterCount() < 1) {
            return null;
        }

        ExecutableParameter.Parameter firstParameter = executableParameter.parameters()[0];
        return resolve(firstParameter.type(), actorId);
    }
}
