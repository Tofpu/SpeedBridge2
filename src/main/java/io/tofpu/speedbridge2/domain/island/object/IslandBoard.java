package io.tofpu.speedbridge2.domain.island.object;

import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.GlobalBoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class IslandBoard extends ConcurrentHashMap<Integer, GlobalBoardPlayer> {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public IslandBoard(final int slot) {
        executor.scheduleWithFixedDelay(() -> {
            try (final DatabaseQuery databaseQuery = new DatabaseQuery(
                    "SELECT * FROM " + "scores WHERE islandSlot " + "= ?" +
                    " ORDER BY score " + "LIMIT 10 OFFSET 0")) {
                databaseQuery.setInt(1, slot);

                try (final ResultSet resultSet = databaseQuery.executeQuery()) {
                    while (resultSet.next()) {
                        final int position = resultSet.getRow();
                        final UUID uuid = UUID.fromString(resultSet.getString("uid"));
                        final BridgePlayer bridgePlayer = PlayerService.INSTANCE.get(uuid);

                        final GlobalBoardPlayer value = new GlobalBoardPlayer(position,
                                uuid, bridgePlayer);
                        put(position, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 10, TimeUnit.SECONDS);
    }
}
