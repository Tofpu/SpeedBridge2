package io.tofpu.speedbridge2.domain.player.object;

import org.bukkit.command.CommandSender;

public class CommonBridgePlayer<T extends CommandSender> {
    public T getPlayer() {
        throw new UnsupportedOperationException("getPlayer have not been " +
                "implemented.");
    }
}
