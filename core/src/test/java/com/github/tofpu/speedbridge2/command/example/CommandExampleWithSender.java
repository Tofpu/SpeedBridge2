package com.github.tofpu.speedbridge2.command.example;

import com.github.tofpu.speedbridge2.command.PlayerCommandActor;
import com.github.tofpu.speedbridge2.command.CommandActor;
import com.github.tofpu.speedbridge2.command.ConsoleCommandActor;
import com.github.tofpu.speedbridge2.command.annontation.Command;
import com.github.tofpu.speedbridge2.command.annontation.Join;
import com.github.tofpu.speedbridge2.command.annontation.Subcommand;

@Command(name = "root")
public class CommandExampleWithSender {
    @Subcommand(name = "common")
    public void common(final CommandActor actor, final @Join String[] args) {
        actor.sendMessage(String.join(" ", args));
    }

    @Subcommand(name = "console")
    public void console(final ConsoleCommandActor actor, final @Join String[] args) {
        actor.sendMessage(String.join(" ", args));
    }

    @Subcommand(name = "player")
    public void player(final PlayerCommandActor actor, final @Join String[] args) {
        actor.sendMessage(String.join(" ", args));
    }
}
