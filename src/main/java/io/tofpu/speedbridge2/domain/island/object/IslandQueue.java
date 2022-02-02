package io.tofpu.speedbridge2.domain.island.object;

import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.GlobalBoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.TimeUnit;

public final class IslandQueue {
    public static final long INTERVAL = TimeUnit.SECONDS.toMillis(10);
    private static final Queue<Island> ISLAND_QUEUE = new LinkedList<>();
    private static final Timer QUEUE_TIMER = new Timer();

    public static void add(final Island island) {
        System.out.println(island.getSlot() + " has been added to the queue!");
        ISLAND_QUEUE.add(island);
    }

    public static void remove(final Island island) {
        ISLAND_QUEUE.remove(island);
    }

    public static void load() {
        QUEUE_TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("starting the leaderboard update process!");

                for (final Island island : ISLAND_QUEUE) {
                    System.out.println("updating " + island.getSlot() + " now!");
                    final Map<Integer, GlobalBoardPlayer> boardMap = new HashMap<>();

                    try (final DatabaseQuery databaseQuery = new DatabaseQuery(
                            "SELECT * FROM scores WHERE island_slot = ? ORDER BY " +
                            "score " + "LIMIT 10 OFFSET 0")) {
                        databaseQuery.setInt(1, island.getSlot());

                        try (final ResultSet resultSet = databaseQuery.executeQuery()) {
                            while (resultSet.next()) {
                                final int position = resultSet.getRow();
                                final UUID uuid = UUID.fromString(resultSet.getString("uid"));
                                final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(uuid);

                                final GlobalBoardPlayer value = new GlobalBoardPlayer(position, uuid, bridgePlayer);

                                boardMap.put(position, value);
                            }
                        }

                        System.out.println("successfully updated " + island.getSlot() + " island!");
                        System.out.println(boardMap);
                        island.updateBoard(boardMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, INTERVAL, INTERVAL);
    }

    public static void shutdown() {
        QUEUE_TIMER.cancel();
    }
}
