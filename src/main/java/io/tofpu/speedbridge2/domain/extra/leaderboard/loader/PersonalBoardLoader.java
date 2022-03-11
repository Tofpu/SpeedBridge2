package io.tofpu.speedbridge2.domain.extra.leaderboard.loader;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.tofpu.speedbridge2.domain.common.PlayerNameCache;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.domain.common.database.wrapper.DatabaseSet;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.extra.leaderboard.meta.BoardRetrieve;
import io.tofpu.speedbridge2.domain.extra.leaderboard.wrapper.BoardPlayer;
import io.tofpu.speedbridge2.domain.player.PlayerService;
import io.tofpu.speedbridge2.domain.player.misc.score.Score;
import io.tofpu.speedbridge2.domain.player.object.BridgePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class PersonalBoardLoader extends CacheLoader<UUID, BoardPlayer> implements BoardRetrieve<BoardPlayer> {
    public static final PersonalBoardLoader INSTANCE = new PersonalBoardLoader();
    private static final String GLOBAL_POSITION = "SELECT DISTINCT 1 + COUNT(*) AS " +
                                                  "position FROM scores WHERE score < (SELECT score FROM scores WHERE uid = ?)";

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
        BridgeUtil.debug("PersonalBoardLoader#retrieve(): key: " + key);
        try (final DatabaseQuery databaseQuery = DatabaseQuery.query(GLOBAL_POSITION)) {
            databaseQuery.setString(key.toString());

            final AtomicReference<BoardPlayer> boardPlayer = new AtomicReference<>();
            databaseQuery.executeQuery(resultSet -> {
                BridgeUtil.debug("PersonalBoardLoader#retrieve(): executeQuery:");
                if (!resultSet.next()) {
                    System.out.println("PersonalBoardLoader#retrieve(): next: " + "false");
                    return;
                }

                BridgeUtil.debug("PersonalBoardLoader#retrieve(): next: " + "true");
                BoardPlayer value = toBoardPlayer(key, resultSet);
                final BridgePlayer player = PlayerService.INSTANCE.get(key);
                if (player != null && player.getScores().isEmpty()) {
                    value = new BoardPlayer(value.getName(), 0, key, value.getScore());
                }

                boardPlayer.set(value);
            });

            final BoardPlayer player = boardPlayer.get();

            BridgeUtil.debug("PersonalBoardLoader#retrieve(): player: " + player);
            if (player == null) {
                return new BoardPlayer(PlayerNameCache.INSTANCE.getOrDefault(key),
                        0, key, new Score(-1, -1));
            }
            return player;
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public BoardPlayer toBoardPlayer(final UUID uid, final DatabaseSet databaseSet) {
        return new BoardPlayer(PlayerNameCache.INSTANCE.getOrDefault(uid),
                databaseSet.getInt("position"), uid,
                new Score(-1, -1));
    }
}
