package io.tofpu.speedbridge2.model.player.object;

import org.bukkit.command.CommandSender;

public class CommonBridgePlayer<T extends CommandSender> {
    public String getName() {
        throw new UnsupportedOperationException("getName have not been " +
                "implemented.");
    }

    public T getPlayer() {
        throw new UnsupportedOperationException("getPlayer have not been " +
                "implemented.");
    }
}
