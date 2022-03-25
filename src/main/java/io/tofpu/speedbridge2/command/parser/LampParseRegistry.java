package io.tofpu.speedbridge2.command.parser;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.command.AbstractLampRegistry;

@AutoRegister
public final class LampParseRegistry extends AbstractLampRegistry<Class<?>,
        AbstractLampParser<?>> {}
