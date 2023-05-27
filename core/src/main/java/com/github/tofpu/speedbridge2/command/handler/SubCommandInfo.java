package com.github.tofpu.speedbridge2.command.handler;

public abstract class SubCommandInfo {
    private final String name;

    protected SubCommandInfo(String name) {
        this.name = name;
    }

    public abstract void invoke(String[] arguments);

    public String name() {
        return name;
    }
}
