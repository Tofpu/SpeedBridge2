package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.executable.Executable;
import java.util.List;

public interface CommandDetail extends Executable {

    String name();

    CommandDetail findNested(String commandName);

    List<? extends SubCommandDetail> subcommands();
}
