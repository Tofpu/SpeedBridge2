package io.tofpu.speedbridge2.domain.common.util;

import io.tofpu.speedbridge2.SpeedBridge;
import io.tofpu.speedbridge2.domain.common.PlayerNameCache;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseSet;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.extra.CommonBridgePlayer;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

import java.util.UUID;

public final class BridgeUtil {
    public static Vector toVector(final com.sk89q.worldedit.Vector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static double nanoToSeconds(final long end) {
        return (double) (System.nanoTime() - end) / 1_000_000_000;
    }

    public static String formatNumber(final double score) {
        return String.format("%.3f", score);
    }

    public static String translateMiniMessageLegacy(final String content) {
        return BukkitComponentSerializer.legacy().serialize(translateMiniMessage(content));
    }

    public static Component translateMiniMessage(final String content) {
        return MiniMessage.miniMessage().deserializeOrNull(content);
    }

    public static Component sendMessage(final CommandSender sender,
            final Component component) {
        SpeedBridge.getAdventure().sender(sender).sendMessage(component);
        return component;
    }

    public static Component sendMessage(final CommonBridgePlayer<?> sender,
            final String content) {
        final Component component = translateMiniMessage(content);
        sendMessage(sender.getPlayer(), component);
        return component;
    }

    public static Component sendMessage(final CommandSender sender,
            final String content) {
        final Component component = translateMiniMessage(content);
        sendMessage(sender, component);
        return component;
    }

    public static void debug(final String message) {
        if (ConfigurationManager.INSTANCE.getGeneralCategory()
                .isDebugEnabled()) {
            Bukkit.getLogger()
                    .info("[DEBUG] " + message);
        }
    }

    public static String translate(final String replace) {
        return ChatColor.translateAlternateColorCodes('&', replace);
    }

    public static BoardPlayer resultToBoardPlayer(final boolean row, final DatabaseSet databaseSet) {
        final String resultUid = databaseSet.getString("uid");
        if (resultUid == null) {
            return null;
        }

        final UUID uid = UUID.fromString(resultUid);
        final int islandSlot = databaseSet.getInt("island_slot");
        final double playerScore = databaseSet.getDouble("score");

        final Score score = Score.of(islandSlot, playerScore);

        BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(uid);
        if (bridgePlayer == null) {
            bridgePlayer = BridgePlayer.of(PlayerNameCache.INSTANCE.getOrDefault(uid), uid);
        }

        final String name = bridgePlayer.getName();
        final int position = !row ? databaseSet.getInt("position") :
                databaseSet.getRow();

        return new BoardPlayer(name, position, uid, score);
    }
}
