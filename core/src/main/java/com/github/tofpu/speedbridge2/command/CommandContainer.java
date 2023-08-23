package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.internal.DefaultCommand;

import java.util.List;

public interface CommandContainer {

    String name();

    CommandContainer findNested(String commandName);

    DefaultCommand defaultCommand();
    List<? extends SubCommand> subcommands();
}
