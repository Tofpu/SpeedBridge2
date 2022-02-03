package io.tofpu.speedbridge2.domain.leaderboard.loader;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.leaderboard.meta.BoardRetrieve;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.IslandBoardPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class IslandLoader extends CacheLoader<UUID, IslandBoardPlayer> implements BoardRetrieve<IslandBoardPlayer> {
    public static final IslandLoader INSTANCE = new IslandLoader();

    private IslandLoader() {}

    @Override
    public @NotNull IslandBoardPlayer load(final @NotNull UUID key) throws Exception {
        BridgeUtil.debug("attempting to load " + key);
        return retrieve(key);
    }

    @Override
    public ListenableFuture<IslandBoardPlayer> reload(final @NotNull UUID key, final @NotNull IslandBoardPlayer oldValue) {
        BridgeUtil.debug("attempting to reload " + key);
        return Futures.immediateFuture(retrieve(key));
    }

    @Override
    public @NotNull IslandBoardPlayer retrieve(final @NotNull UUID key) {
        BridgeUtil.debug("retrieving " + key);
        return new IslandBoardPlayer(key);
    }
}
