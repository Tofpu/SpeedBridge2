package io.tofpu.speedbridge2.domain.leaderboard;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.tofpu.speedbridge2.domain.leaderboard.meta.BoardRetrieve;
import io.tofpu.speedbridge2.domain.leaderboard.wrapper.IslandPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class IslandLoader extends CacheLoader<UUID, IslandPlayer> implements BoardRetrieve<IslandPlayer> {
    public static final IslandLoader INSTANCE = new IslandLoader();

    private IslandLoader() {}

    @Override
    public @NotNull IslandPlayer load(final @NotNull UUID key) throws Exception {
        System.out.println("attempting to load " + key);
        return retrieve(key);
    }

    @Override
    public ListenableFuture<IslandPlayer> reload(final @NotNull UUID key, final @NotNull IslandPlayer oldValue) {
        System.out.println("attempting to reload " + key);
        return Futures.immediateFuture(retrieve(key));
    }

    @Override
    public @NotNull IslandPlayer retrieve(final @NotNull UUID key) {
        System.out.println("retrieving " + key);
        return new IslandPlayer(key);
    }
}
