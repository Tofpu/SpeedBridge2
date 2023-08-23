package com.github.tofpu.speedbridge2.command.resolve;

import com.github.tofpu.speedbridge2.command.internal.executable.Executable;

public class ResolvedCommand {

    private final Executable executable;
    private final String[] arguments;

    public ResolvedCommand(Executable executable, String[] arguments) {
        this.executable = executable;
        this.arguments = arguments;
    }

    public Executable executable() {
        return executable;
    }

    public String[] arguments() {
        return arguments;
    }

}
