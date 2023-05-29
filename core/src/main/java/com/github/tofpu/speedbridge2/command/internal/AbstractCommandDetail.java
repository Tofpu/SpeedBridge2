package com.github.tofpu.speedbridge2.command.internal;

import com.github.tofpu.speedbridge2.command.CommandDetail;
import com.github.tofpu.speedbridge2.command.SubCommandDetail;
import java.util.List;

public abstract class AbstractCommandDetail implements CommandDetail {
    private final String name;
    private final List<? extends SubCommandDetail> subcommands;

    public AbstractCommandDetail(String name, List<? extends SubCommandDetail> subcommands) {
        this.name = name;
        this.subcommands = subcommands;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<? extends SubCommandDetail> subcommands() {
        return subcommands;
    }
}
