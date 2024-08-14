package io.tofpu.speedbridge2.model.common.util;

import io.tofpu.speedbridge2.SpeedBridge;
import io.tofpu.speedbridge2.model.common.PlayerNameCache;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseSet;
import io.tofpu.speedbridge2.model.leaderboard.object.BoardPlayer;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import io.tofpu.speedbridge2.model.player.object.CommonBridgePlayer;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.plugin.SpeedBridgePlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * This class is a utility class that provides utility methods for the SpeedBridge plugin
 */
public final class BridgeUtil {
    /**
     * A function used to log info-related messages to Bukkit's logger.
     * This function also does add this plugin's name as a prefix
     * to differentiate the message from one plugin to another
     *
     * @param content the content that will be printed
     */
    public static void log(final String content) {
        Bukkit.getLogger().info("[Speedbridge2] " + content);
    }

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
    public static String miniMessageToLegacy(final String content) {
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
     * @param sender    The CommandSender who is sending the message.
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
     * @param sender  The player who sent the message.
     * @param content The message to send.
     * @return Nothing.
     */
    public static Component sendMessage(final CommonBridgePlayer<?> sender,
                                        final String content) {
        if (content.isEmpty()) {
            return null;
        }
        final Component component = translateMiniMessage(content);
        sendMessage(sender.getPlayer(), component);
        return component;
    }

    /**
     * Send a message to a
     * command sender
     *
     * @param sender  The CommandSender who will receive the message.
     * @param content The content of the message.
     * @return Nothing.
     */
    public static Component sendMessage(final CommandSender sender, String content) {
        if (content.isEmpty()) {
            return null;
        }
        
        if (sender instanceof Player) {
            content = replaceWithPAPI((Player) sender, content);
        }
        
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

    public static String replaceWithPAPI(Player player, String text) {
        if (player == null) {
            return text;
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return text;
        }
        return PlaceholderAPI.setBracketPlaceholders(player.getPlayer(), text);
    }

    /**
     * Given a row or column, and a database set, return a BoardPlayer
     *
     * @param row         boolean
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

        final String name = PlayerNameCache.INSTANCE.getOrDefault(uid);
        final int position = !row ? databaseSet.getInt("position") : databaseSet.getRow();

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
     * @param delay    The delay is the amount of time to wait before the first execution.
     * @param interval How often the task should run.
     */
    public static void runBukkitAsync(final Runnable runnable, final long delay,
                                      final long interval) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(JavaPlugin.getPlugin(SpeedBridgePlugin.class), runnable, delay, interval);
    }

    /**
     * It runs the given Runnable when the CompletableFuture is completed.
     *
     * @param completableFuture    The completable future to be completed.
     * @param whenCompleteConsumer The consumer to be run when the completable future is completed.
     */
    public static <T> CompletableFuture<T> whenComplete(final CompletableFuture<T> completableFuture,
                                                        final Consumer<T> whenCompleteConsumer) {
        return completableFuture.whenComplete((o, throwable) -> {
            if (throwable != null) {
                throw new IllegalStateException(throwable);
            }
            whenCompleteConsumer.accept(o);
        });
    }

    /**
     * It runs the given Runnable when the CompletableFuture is completed.
     *
     * @param completableFuture The completable future to be completed.
     * @param whenComplete      The runnable to be run when the completable future is
     *                          completed.
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
