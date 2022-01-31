package io.tofpu.speedbridge2.domain.leaderboard.wrapper;

import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class IslandPlayer {
    private static final String ISLAND_POSITION =
            "SELECT 1 + COUNT(*) AS position FROM scores WHERE islandSlot = ? AND score" +
            " < " + "(SELECT score " + "FROM scores WHERE uid = ?)";

    private final UUID owner;
    private final Map<Integer, IslandBoard> boardMap;

    public IslandPlayer(final UUID owner) {
        this.owner = owner;
        this.boardMap = new ConcurrentHashMap<>();
    }

    public @NotNull CompletableFuture<IslandBoard> retrieve(final int islandSlot) {
        System.out.println("attempting to retrieve board for " + owner);

        // TODO: improve this later
        if (boardMap.containsKey(islandSlot)) {
            System.out.println("found existing board " + owner);
            return CompletableFuture.completedFuture(boardMap.get(islandSlot));
        }

        System.out.println("attempting to query to database for board for " + owner);
        try (final DatabaseQuery databaseQuery = new DatabaseQuery(ISLAND_POSITION)) {
            databaseQuery.setInt(1, islandSlot);
            databaseQuery.setString(2, owner.toString());

            try (final ResultSet resultSet = databaseQuery.executeQuery()) {
                final IslandBoard islandBoard = new IslandBoard(resultSet.getInt(1), islandSlot);
                boardMap.put(islandSlot, islandBoard);
                return CompletableFuture.completedFuture(islandBoard);
            }
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
