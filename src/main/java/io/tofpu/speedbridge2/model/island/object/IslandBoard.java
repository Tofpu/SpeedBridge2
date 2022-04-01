package io.tofpu.speedbridge2.model.island.object;

import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.leaderboard.wrapper.BoardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class IslandBoard {
    public static final long INTERVAL = ConfigurationManager.INSTANCE.getLeaderboardCategory()
            .getGlobalUpdateInterval();

    private static final Queue<Island> ISLAND_QUEUE = new ConcurrentLinkedQueue<>();

    public static void add(final Island island) {
        BridgeUtil.debug(island.getSlot() + " has been added to the queue!");
        ISLAND_QUEUE.add(island);
    }

    public static void remove(final Island island) {
        ISLAND_QUEUE.remove(island);
    }

    public static CompletableFuture<?> load(final JavaPlugin javaPlugin) {
        final CompletableFuture<?> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler()
                .runTaskAsynchronously(javaPlugin, () -> {
                    BridgeUtil.debug("IslandBoard#load(): loading the island's " +
                                     "leaderboard");

                    for (final Island island : ISLAND_QUEUE) {
                        BridgeUtil.debug("IslandBoard#load(): Loading " + island.getSlot() + " leaderboard " +
                                         "now!");
                        final Map<Integer, BoardPlayer> boardMap = new HashMap<>();

                        try (final DatabaseQuery databaseQuery = DatabaseQuery.query(
                                "SELECT * FROM scores WHERE island_slot = ? ORDER BY" +
                                " " +
                                "score " + "LIMIT 10 OFFSET 0")) {
                            databaseQuery.setInt(island.getSlot());

                            final Map<UUID, BoardPlayer> boardPlayerMap =
                                    new HashMap<>();

                            databaseQuery.executeQuery(resultSet -> {
                                while (resultSet.next()) {
                                    final BoardPlayer value = BridgeUtil.toBoardPlayer(true, resultSet);
                                    if (value == null) {
                                        continue;
                                    }

                                    final UUID owner = value.getOwner();
                                    if (boardPlayerMap.containsKey(owner)) {
                                        continue;
                                    }

                                    boardMap.put(value.getPosition(), value);
                                    boardPlayerMap.put(owner, value);
                                }
                            });

                            BridgeUtil.debug("IslandBoard#load(): Successfully loaded " + island.getSlot() +
                                             " island leaderboard!");
                            BridgeUtil.debug(String.valueOf(boardMap));
                            island.loadBoard(boardMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    completableFuture.complete(null);
                });

        Bukkit.getScheduler()
                .runTaskTimerAsynchronously(javaPlugin, () -> {
                    BridgeUtil.debug("IslandBoard#load(): Starting the leaderboard " +
                                     "update process!");

                    for (final Island island : ISLAND_QUEUE) {
                        BridgeUtil.debug("IslandBoard#load(): Updating " + island.getSlot() + " now!");

                        island.updateLeaderboard();
                    }
                }, 1L, 20 * INTERVAL);

        return completableFuture;
    }

    public static void reset(final UUID uuid) {
        for (final Island island : ISLAND_QUEUE) {
            island.resetPlayer(uuid);
        }
    }
}
