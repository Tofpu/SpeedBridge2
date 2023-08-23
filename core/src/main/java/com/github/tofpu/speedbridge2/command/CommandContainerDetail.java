package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.internal.DefaultCommand;

import java.util.List;

public interface CommandContainerDetail {

    String name();

    CommandContainerDetail findNested(String commandName);

    DefaultCommand defaultCommand();
    List<? extends SubCommandDetail> subcommands();
}
