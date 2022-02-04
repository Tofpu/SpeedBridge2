package io.tofpu.speedbridge2.domain.island.object;

import io.tofpu.speedbridge2.domain.common.config.manager.ConfigurationManager;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.TimeUnit;

public final class IslandBoard {
    public static final long INTERVAL = TimeUnit.SECONDS.toMillis(ConfigurationManager.INSTANCE.getLeaderboardCategory()
            .getUpdateInterval());

    private static final Queue<Island> ISLAND_QUEUE = new LinkedList<>();
    private static final Timer QUEUE_TIMER = new Timer();

    public static void add(final Island island) {
        BridgeUtil.debug(island.getSlot() + " has been added to the queue!");
        ISLAND_QUEUE.add(island);
    }

    public static void remove(final Island island) {
        ISLAND_QUEUE.remove(island);
    }

    public static void load() {
        QUEUE_TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                BridgeUtil.debug("starting the leaderboard update process!");

                for (final Island island : ISLAND_QUEUE) {
                    BridgeUtil.debug("updating " + island.getSlot() + " now!");
                    final Map<Integer, BoardPlayer> boardMap = new HashMap<>();

                    try (final DatabaseQuery databaseQuery = new DatabaseQuery(
                            "SELECT * FROM scores WHERE island_slot = ? ORDER BY " +
                            "score " + "LIMIT 10 OFFSET 0")) {
                        databaseQuery.setInt(1, island.getSlot());

                        try (final ResultSet resultSet = databaseQuery.executeQuery()) {
                            while (resultSet.next()) {
                                final int position = resultSet.getRow();
                                final UUID uuid = UUID.fromString(resultSet.getString("uid"));
//                                final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(uuid);

                                final int islandSlot = resultSet.getInt("island_slot");
                                final double playerScore = resultSet.getDouble("score");
                                final Score score = Score.of(islandSlot, playerScore);

                                final BoardPlayer value = new BoardPlayer(position, uuid, score);

                                boardMap.put(position, value);
                            }
                        }

                        BridgeUtil.debug("successfully updated " + island.getSlot() + " island!");
                        BridgeUtil.debug(String.valueOf(boardMap));
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
