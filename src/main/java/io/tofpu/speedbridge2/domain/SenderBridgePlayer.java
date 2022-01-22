package io.tofpu.speedbridge2.domain;

import org.bukkit.command.CommandSender;

public final class SenderBridgePlayer extends CommonBridgePlayer<CommandSender> {
    private final CommandSender sender;

    public SenderBridgePlayer(final CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public CommandSender getPlayer() {
        return getSender();
    }

    public CommandSender getSender() {
        return sender;
    }
}
