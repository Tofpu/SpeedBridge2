package io.tofpu.speedbridge2.util;

import io.tofpu.speedbridge2.SpeedBridge;
import io.tofpu.speedbridge2.domain.player.object.CommonBridgePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

public final class BridgeUtil {
    public static Vector toVector(final com.sk89q.worldedit.Vector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static String toFormattedScore(final double score) {
        return String.format("%.3f", score);
    }

    public static Component translateMiniMessage(final String content) {
        return MiniMessage.get().parse(content);
    }

    public static Component sendMessage(final CommandSender sender,
            final Component component) { ;
        SpeedBridge.getAdventure().sender(sender).sendMessage(component);
        return component;
    }

    public static Component sendMessage(final CommandSender sender,
            final String content) {
        final Component component = translateMiniMessage(content);
        sendMessage(sender, component);
        return component;
    }
}
