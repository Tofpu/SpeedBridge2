package io.tofpu.speedbridge2.command.condition;

import io.tofpu.dynamicclass.meta.AutoRegister;
import io.tofpu.speedbridge2.command.AbstractLampRegistry;

import java.lang.annotation.Annotation;

@AutoRegister
public final class LampConditionRegistry extends AbstractLampRegistry<Class<?
        extends Annotation>, AbstractCommandConditionWrapper> {
}
