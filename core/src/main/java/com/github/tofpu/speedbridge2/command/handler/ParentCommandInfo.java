package com.github.tofpu.speedbridge2.command.handler;

import java.util.HashSet;
import java.util.Set;

public abstract class ParentCommandInfo {
    private final String name;
    private final Set<SubCommandInfo> subcommands = new HashSet<>();

    public ParentCommandInfo(String name, Set<? extends SubCommandInfo> subcommands) {
        this.name = name;
        this.subcommands.addAll(subcommands);
    }

    public abstract void defaultInvoke();
    public abstract void defaultInvoke(Object[] arguments);

    public abstract ParentCommandInfo find(final String nestedCommand);

    public String name() {
        return name;
    }

    public Set<SubCommandInfo> subcommands() {
        return new HashSet<>(subcommands);
    }
}
