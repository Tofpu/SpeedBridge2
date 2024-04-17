package io.tofpu.speedbridge2.command.context;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.command.AbstractLampRegistry;

@AutoRegister
public final class LampContextRegistry extends AbstractLampRegistry<Class<?>,
        AbstractLampContext<?>> {
}
