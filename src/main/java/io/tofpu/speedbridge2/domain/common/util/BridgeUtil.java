package io.tofpu.speedbridge2.domain.common.util;

import io.tofpu.speedbridge2.SpeedBridge;
import io.tofpu.speedbridge2.domain.common.PlayerNameCache;
import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseSet;
import io.tofpu.speedbridge2.domain.extra.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import io.tofpu.speedbridge2.domain.player.object.extra.CommonBridgePlayer;
import io.tofpu.speedbridge2.plugin.SpeedBridgePlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is a utility class that provides utility methods for the SpeedBridge plugin
 */
public final class BridgeUtil {
    /**
     * Convert nanoseconds to seconds
     *
     * @param end The time at which the timer should stop.
     * @return The time in seconds.
     */
    public static double nanoToSeconds(final long end) {
        return (double) (System.nanoTime() - end) / 1_000_000_000;
    }

    /**
     * Given a double, return a string of that double rounded to 3 decimal places
     *
     * @param score The score to format.
     * @return The string "0.000"
     */
    public static String formatNumber(final double score) {
        return String.format("%.3f", score);
    }

    /**
     * It takes a string, and returns a string
     *
     * @param content The content of the message.
     * @return The translated message.
     */
    public static String translateMiniMessageLegacy(final String content) {
        return BukkitComponentSerializer.legacy().serialize(translateMiniMessage(content));
    }

    /**
     * It takes a string and returns a component
     *
     * @param content The content of the message.
     * @return Nothing.
     */
    public static Component translateMiniMessage(final String content) {
        return MiniMessage.miniMessage().deserializeOrNull(content);
    }

    /**
     * Send a message to a
     * command sender
     *
     * @param sender The CommandSender who is sending the message.
     * @param component The component to send.
     * @return The component that was sent.
     */
    public static Component sendMessage(final CommandSender sender,
            final Component component) {
        final Audience audience = SpeedBridge.getAdventure()
                .sender(sender);
        audience.sendMessage(component);
        return component;
    }

    /**
     * This function sends a message to a player
     *
     * @param sender The player who sent the message.
     * @param content The message to send.
     * @return Nothing.
     */
    public static Component sendMessage(final CommonBridgePlayer<?> sender,
            final String content) {
        final Component component = translateMiniMessage(content);
        sendMessage(sender.getPlayer(), component);
        return component;
    }

    /**
     * Send a message to a
     * command sender
     *
     * @param sender The CommandSender who will receive the message.
     * @param content The content of the message.
     * @return Nothing.
     */
    public static Component sendMessage(final CommandSender sender,
            final String content) {
        final Component component = translateMiniMessage(content);
        sendMessage(sender, component);
        return component;
    }

    /**
     * If the debug flag is
     * enabled, print the message to the console
     *
     * @param message The message to be logged.
     */
    public static void debug(final String message) {
        if (ConfigurationManager.INSTANCE.getGeneralCategory()
                .isDebugEnabled()) {
            Bukkit.getLogger()
                    .info("[DEBUG] " + message);
        }
    }

    /**
     * It takes a string and replaces all instances of the color codes with the actual color
     * codes
     *
     * @param replace The string to replace.
     * @return Nothing.
     */
    public static String translate(final String replace) {
        return ChatColor.translateAlternateColorCodes('&', replace);
    }

    /**
     * Given a row or column, and a database set, return a BoardPlayer
     *
     * @param row boolean
     * @param databaseSet The database set that is being converted to a BoardPlayer.
     * @return A BoardPlayer object.
     */
    public static BoardPlayer toBoardPlayer(final boolean row, final DatabaseSet databaseSet) {
        final String resultUid = databaseSet.getString("uid");
        if (resultUid == null) {
            BridgeUtil.debug("BridgeUtil#toBoardPlayer(): uid == null");
            return null;
        }

        final UUID uid = UUID.fromString(resultUid);
        final int islandSlot = databaseSet.getInt("island_slot");
        final double playerScore = databaseSet.getDouble("score");

        final Score score = Score.of(islandSlot, playerScore);

        BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(uid);
        if (bridgePlayer == null) {
            BridgeUtil.debug("BridgeUtil#toBoardPlayer(): bridgePlayer == null : " + uid);
            bridgePlayer = BridgePlayer.of(PlayerNameCache.INSTANCE.getOrDefault(uid), uid);
        } else {
            BridgeUtil.debug("BridgeUtil#toBoardPlayer(): bridgePlayer != null : " + uid);
        }

        final String name = bridgePlayer.getName();
        final int position = !row ? databaseSet.getInt("position") :
                databaseSet.getRow();

        BridgeUtil.debug("BridgeUtil#toBoardPlayer(): position == " + position);

        return new BoardPlayer(name, position, uid, score);
    }

    /**
     * Find the UUID of a player by their name
     *
     * @param playerName The name of the player to find the UUID of.
     * @return Nothing.
     */
    public static UUID findUUIDBy(final String playerName) {
        final AtomicReference<UUID> uuid = new AtomicReference<>();
        try (final DatabaseQuery databaseQuery = DatabaseQuery.query("SELECT uid FROM " +
                                                                   "players WHERE name " +
                                                                   "= ?")) {
            databaseQuery.setString(playerName);

            databaseQuery.executeQuery(databaseSet -> {
                if (!databaseSet.next()) {
                    return;
                }
                final String uid = databaseSet.getString("uid");
                if (uid != null) {
                    uuid.set(UUID.fromString(uid));
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return uuid.get();
    }

    /**
     * This function runs a runnable on the bukkit scheduler asynchronously
     *
     * @param runnable The Runnable to run.
     */
    public static void runBukkitAsync(final Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SpeedBridgePlugin.class), runnable);
    }

    /**
     * This function will run a task asynchronously
     *
     * @param runnable The Runnable to run.
     * @param delay The delay is the amount of time to wait before the first execution.
     * @param interval How often the task should run.
     */
    public static void runBukkitAsync(final Runnable runnable, final long delay,
            final long interval) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(JavaPlugin.getPlugin(SpeedBridgePlugin.class), runnable, delay, interval);
    }

    /**
     * It runs the given Runnable when the CompletableFuture is completed.
     *
     * @param completableFuture The completable future to be completed.
     * @param whenComplete A Runnable that will be run when the CompletableFuture is
     * completed.
     */
    public static void whenComplete(final CompletableFuture<?> completableFuture,
            final Runnable whenComplete) {
        completableFuture.whenComplete((o, throwable) -> {
            if (throwable != null) {
                throw new IllegalStateException(throwable);
            }
            whenComplete.run();
        });
    }

    public static void runBukkitSync(final Runnable runnable, final int delay) {
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(SpeedBridgePlugin.class),
                runnable, delay);
    }
}
