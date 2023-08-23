package com.github.tofpu.speedbridge2.command.mapper;

public interface CommandMapper<C> {
    C map(Object owner);
}
