package com.github.tofpu.speedbridge2.command;

import java.util.List;

public interface CommandDetail {
    String name();
    CommandDetail findNested(String commandName);
    void executeDefault(Object... args);
    List<? extends SubCommandDetail> subcommands();
}
