package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.executable.Executable;

public interface SubCommand extends Executable {

    String name();
}
