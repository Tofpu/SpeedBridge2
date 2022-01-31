package io.tofpu.speedbridge2.domain.leaderboard;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;

import java.sql.ResultSet;
import java.util.UUID;

public final class BoardLoader extends CacheLoader<UUID, BoardPlayer> {
    public static final BoardLoader INSTANCE = new BoardLoader();
    private static final String SQL =
            "SELECT 1 + COUNT(*) AS position FROM scores WHERE score < (SELECT score " +
            "FROM scores WHERE uid = ?)";

    private BoardLoader() {}

    @Override
    public BoardPlayer load(final UUID key) throws Exception {
        return retrieve(key);
    }

    @Override
    public ListenableFuture<BoardPlayer> reload(final UUID key, final BoardPlayer oldValue) throws Exception {
        return Futures.immediateFuture(retrieve(key));
    }

    private BoardPlayer retrieve(final UUID key) {
        try (final DatabaseQuery databaseQuery = new DatabaseQuery(SQL)) {
            databaseQuery.setString(1, key.toString());
            try (final ResultSet resultSet = databaseQuery.executeQuery()) {
                return new BoardPlayer(resultSet.getInt(1), PlayerService.INSTANCE.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
