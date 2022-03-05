package io.tofpu.speedbridge2.domain.leaderboard.loader;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.tofpu.speedbridge2.domain.common.PlayerNameCache;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.leaderboard.meta.BoardRetrieve;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class PersonalBoardLoader extends CacheLoader<UUID, BoardPlayer> implements BoardRetrieve<BoardPlayer> {
    public static final PersonalBoardLoader INSTANCE = new PersonalBoardLoader();
    private static final String GLOBAL_POSITION =
            "SELECT * FROM (SELECT *, COUNT(*) AS" + " position FROM scores" +
                                                  ") WHERE uid = ?";

    private PersonalBoardLoader() {}

    @Override
    public @Nullable BoardPlayer load(final @NotNull UUID key) throws Exception {
        return retrieve(key);
    }

    @Override
    public ListenableFuture<BoardPlayer> reload(final @NotNull UUID key, final @NotNull BoardPlayer oldValue) {
        return Futures.immediateFuture(retrieve(key));
    }

    @Override
    public @Nullable BoardPlayer retrieve(final @NotNull UUID key) {
        try (final DatabaseQuery databaseQuery = new DatabaseQuery(GLOBAL_POSITION)) {
            databaseQuery.setString(key.toString());

            final AtomicReference<BoardPlayer> boardPlayer = new AtomicReference<>();
            databaseQuery.executeQuery(resultSet -> {
                if (!resultSet.next()) {
                    return;
                }
                boardPlayer.set(BridgeUtil.resultToBoardPlayer(false, resultSet));
            });

            final BoardPlayer player = boardPlayer.get();
            if (player == null) {
                return new BoardPlayer(PlayerNameCache.INSTANCE.getOrDefault(key),
                        0, key, new Score(-1, -1));
            }
            return player;
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
