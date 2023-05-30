package com.github.tofpu.speedbridge2.command;

import com.github.tofpu.speedbridge2.command.executable.Executable;

public class CommandExecutor {

    public void execute(ExecutableCommand command) {
        Executable executable = command.executable;
        Object[] args = command.arguments;

        executable.invoke(args);
    }

    static class ExecutableCommand {

        private final Executable executable;
        private final Object[] arguments;

        public ExecutableCommand(Executable executable, Object[] arguments) {
            this.executable = executable;
            this.arguments = arguments;
        }
    }
}
