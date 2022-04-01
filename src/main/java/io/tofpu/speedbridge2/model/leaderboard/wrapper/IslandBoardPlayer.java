package io.tofpu.speedbridge2.model.leaderboard.wrapper;

import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public final class IslandBoardPlayer {
    private static final String ISLAND_POSITION =
            "SELECT 1 + COUNT(*) AS position FROM scores WHERE island_slot = ? AND " +
            "score" +
            " < " + "(SELECT score " + "FROM scores WHERE uid = ?)";

    private final UUID owner;
    private final Map<Integer, IslandBoard> boardMap;

    public IslandBoardPlayer(final UUID owner) {
        this.owner = owner;
        this.boardMap = new ConcurrentHashMap<>();
    }

    public @Nullable IslandBoard findDefault(final int islandSlot) {
        return boardMap.get(islandSlot);
    }

    public @NotNull CompletableFuture<IslandBoard> retrieve(final int islandSlot) {
        BridgeUtil.debug("IslandBoardPlayer#retrieve(): Attempting to retrieve board " +
                         "for " + owner + ", " + islandSlot);

        final IslandBoard cachedValue = boardMap.get(islandSlot);
        // if the cached value is not null
        if (cachedValue != null) {
            // return the cached value
            BridgeUtil.debug("IslandBoardPlayer#retrieve(): Found existing value " + owner + ", " + islandSlot);
            return CompletableFuture.completedFuture(cachedValue);
        }

        BridgeUtil.debug("IslandBoardPlayer#retrieve(): Attempting to query to database" +
                         " for position for " + owner +
                         ", " + islandSlot);
        try (final DatabaseQuery databaseQuery = DatabaseQuery.query(ISLAND_POSITION)) {
            databaseQuery.setInt(islandSlot);
            databaseQuery.setString(owner.toString());

            final AtomicReference<IslandBoard> islandBoard = new AtomicReference<>();
            databaseQuery.executeQuery(resultSet -> {
                if (!resultSet.next()) {
                    BridgeUtil.debug("IslandBoardPlayer#retrieve(): next: " + "false");
                    return;
                }

                IslandBoard value = new IslandBoard(resultSet.getInt("position"), islandSlot);
                final BridgePlayer player = PlayerService.INSTANCE.get(owner);
                if (player != null && player.getScores().isEmpty()) {
                    value = new IslandBoard(0, islandSlot);
                }

                islandBoard.set(value);
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
