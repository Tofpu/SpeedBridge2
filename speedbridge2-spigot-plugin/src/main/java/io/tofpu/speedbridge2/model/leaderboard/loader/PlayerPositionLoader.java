package io.tofpu.speedbridge2.model.leaderboard.loader;

import com.github.benmanes.caffeine.cache.CacheLoader;
import io.tofpu.speedbridge2.model.common.PlayerNameCache;
import io.tofpu.speedbridge2.model.common.PluginExecutor;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseQuery;
import io.tofpu.speedbridge2.model.common.database.wrapper.DatabaseSet;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.leaderboard.object.BoardPlayer;
import io.tofpu.speedbridge2.model.player.PlayerService;
import io.tofpu.speedbridge2.model.player.object.score.Score;
import io.tofpu.speedbridge2.model.player.object.BridgePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

public final class PlayerPositionLoader implements CacheLoader<UUID, BoardPlayer>, BoardLoader<BoardPlayer> {
    private static final String GLOBAL_POSITION = "SELECT DISTINCT 1 + COUNT(*) AS " +
                                                  "position FROM scores WHERE score < (SELECT score FROM scores WHERE uid = ?)";

    private final PlayerService playerService;

    public PlayerPositionLoader(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public @Nullable BoardPlayer load(final UUID key) throws Exception {
        return retrieve(key);
    }

    @Override
    public @NonNull CompletableFuture<BoardPlayer> asyncLoad(final @NotNull UUID key, final Executor executor) {
        return retrieveAsync(key, executor);
    }

    @Override
    public @NonNull CompletableFuture<BoardPlayer> asyncReload(final UUID key, final BoardPlayer oldValue, final Executor executor) {
        return retrieveAsync(key, executor);
    }

    @Override
    public BoardPlayer retrieve(final @NotNull UUID uniqueId) {
        final CompletableFuture<BoardPlayer> future = retrieveAsync(uniqueId, PluginExecutor.INSTANCE);
        if (!future.isDone()) {
            return null;
        }

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<BoardPlayer> retrieveAsync(final @NotNull UUID key,
            final @NotNull Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
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
                    final BridgePlayer player = playerService.getIfPresent(key);
                    if (player != null && player.getScores().isEmpty()) {
                        value = new BoardPlayer(value.getName(), 0, key, value.getScore());
                    }

                    boardPlayer.set(value);
                });

                final BoardPlayer player = boardPlayer.get();

                BridgeUtil.debug("PersonalBoardLoader#retrieve(): player: " + player);
                if (player == null) {
                    return new BoardPlayer(PlayerNameCache.INSTANCE.getOrDefault(key),
                            0, key, Score.of(-1, -1));
                }
                return player;
            } catch (final Exception e) {
                throw new IllegalStateException(e);
            }
        }, executor);
    }

    public BoardPlayer toBoardPlayer(final UUID uid, final DatabaseSet databaseSet) {
        return new BoardPlayer(PlayerNameCache.INSTANCE.getOrDefault(uid),
                databaseSet.getInt("position"), uid,
                Score.of(-1, -1));
    }
}
