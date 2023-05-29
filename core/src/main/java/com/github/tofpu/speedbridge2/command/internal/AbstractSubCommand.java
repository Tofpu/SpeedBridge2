package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.command.SubCommandDetail;

public abstract class AbstractSubCommand implements SubCommandDetail {
    private final String name;

    public AbstractSubCommand(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }
}
