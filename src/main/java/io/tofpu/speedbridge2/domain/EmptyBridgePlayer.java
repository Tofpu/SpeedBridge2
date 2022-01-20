package io.tofpu.speedbridge2.domain;

import org.bukkit.command.CommandSender;

public final class EmptyBridgePlayer extends BridgePlayer {
    private final CommandSender sender;

    public EmptyBridgePlayer(final CommandSender sender) {
        super(null);
        this.sender = sender;
    }

    public CommandSender getSender() {
        return sender;
    }
}
