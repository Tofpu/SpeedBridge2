package io.tofpu.speedbridge2.domain.island.object;

import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.extra.leaderboard.wrapper.BoardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
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

    public static void load(final JavaPlugin javaPlugin) {
        Bukkit.getScheduler()
                .runTaskAsynchronously(javaPlugin, () -> {
                    BridgeUtil.debug("loading the island's leaderboard");

                    for (final Island island : ISLAND_QUEUE) {
                        BridgeUtil.debug("loading " + island.getSlot() + " leaderboard " +
                                         "now!");
                        final Map<Integer, BoardPlayer> boardMap = new HashMap<>();

                        try (final DatabaseQuery databaseQuery = new DatabaseQuery(
                                "SELECT * FROM scores WHERE island_slot = ? ORDER BY " +
                                "score " + "LIMIT 10 OFFSET 0")) {
                            databaseQuery.setInt(island.getSlot());

                            databaseQuery.executeQuery(resultSet -> {
                                while (resultSet.next()) {
                                    final BoardPlayer value = BridgeUtil.resultToBoardPlayer(true, resultSet);

                                    boardMap.put(value.getPosition(), value);
                                }
                            });

                            BridgeUtil.debug("successfully loaded " + island.getSlot() +
                                             " island leaderboard!");
                            BridgeUtil.debug(String.valueOf(boardMap));
                            island.loadBoard(boardMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bukkit.getScheduler()
                .runTaskTimerAsynchronously(javaPlugin, () -> {
                    BridgeUtil.debug("starting the leaderboard update process!");

                    for (final Island island : ISLAND_QUEUE) {
                        BridgeUtil.debug("updating " + island.getSlot() + " now!");

                        island.updateLeaderboard();
                    }
                }, 1L, 20 * INTERVAL);
    }
}
