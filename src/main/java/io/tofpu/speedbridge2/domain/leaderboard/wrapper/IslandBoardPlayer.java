package io.tofpu.speedbridge2.domain.leaderboard.wrapper;

import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public final class IslandBoardPlayer {
    private static final String ISLAND_POSITION =
            "SELECT 1 + COUNT(*) AS position FROM scores WHERE islandSlot = ? AND score" +
            " < " + "(SELECT score " + "FROM scores WHERE uid = ?)";

    private final UUID owner;
    private final Map<Integer, IslandBoard> boardMap;

    public IslandBoardPlayer(final UUID owner) {
        this.owner = owner;
        this.boardMap = new ConcurrentHashMap<>();
    }

    public @NotNull CompletableFuture<IslandBoard> retrieve(final int islandSlot) {
        BridgeUtil.debug("attempting to retrieve board for " + owner + ", " + islandSlot);

        final IslandBoard cachedValue = boardMap.get(islandSlot);
        // if the cached value is not null
        if (cachedValue != null) {
            // return the cached value

            BridgeUtil.debug("found existing value " + owner + ", " + islandSlot);
            return CompletableFuture.completedFuture(cachedValue);
        }

        BridgeUtil.debug("attempting to query to database for position for " + owner +
                         ", " + islandSlot);
        try (final DatabaseQuery databaseQuery = new DatabaseQuery(ISLAND_POSITION)) {
            databaseQuery.setInt(islandSlot);
            databaseQuery.setString(owner.toString());

            final AtomicReference<IslandBoard> islandBoard = new AtomicReference<>();
            databaseQuery.executeQuery(resultSet -> {
                try {
                    islandBoard.set(new IslandBoard(resultSet.getInt(1), islandSlot));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                boardMap.put(islandSlot, islandBoard.get());
            });
            return CompletableFuture.completedFuture(islandBoard.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(null);
    }

    public UUID getOwner() {
        return owner;
    }

    public static class IslandBoard {
        private final int position;
        private final int islandSlot;

        public IslandBoard(final int position, final int islandSlot) {
            this.position = position;
            this.islandSlot = islandSlot;
        }

        public int getPosition() {
            return position;
        }

        public int getIslandSlot() {
            return islandSlot;
        }
    }
}
