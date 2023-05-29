package com.github.tofpu.speedbridge2.command.maker;

public abstract class CommandMaker<T> {
    public abstract T create(Object object);
}
