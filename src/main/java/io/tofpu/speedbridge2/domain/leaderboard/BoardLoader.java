package io.tofpu.speedbridge2.domain.leaderboard;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.UUID;

public final class BoardLoader extends CacheLoader<UUID, BoardPlayer> {
    public static final BoardLoader INSTANCE = new BoardLoader();
    private static final String GLOBAL_POSITION =
            "SELECT 1 + COUNT(*) AS position FROM scores WHERE score < (SELECT score " +
            "FROM scores WHERE uid = ?)";

    private BoardLoader() {}

    // TODO: MAKE THIS OPERATION ASYNC
    @Override
    public @Nullable BoardPlayer load(final @NotNull UUID key) throws Exception {
        return retrieve(key);
    }

    @Override
    public ListenableFuture<BoardPlayer> reload(final @NotNull UUID key, final @NotNull BoardPlayer oldValue) {
        return Futures.immediateFuture(retrieve(key));
    }

    private @Nullable BoardPlayer retrieve(final @NotNull UUID key) {
        try (final DatabaseQuery databaseQuery = new DatabaseQuery(GLOBAL_POSITION)) {
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
