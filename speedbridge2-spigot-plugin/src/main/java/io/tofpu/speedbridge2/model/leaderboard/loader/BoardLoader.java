package io.tofpu.speedbridge2.model.leaderboard.loader;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface BoardLoader<T> {
    T retrieve(final @NotNull UUID uniqueId);
    CompletableFuture<T> retrieveAsync(final @NotNull UUID uniqueId,
            final @NotNull Executor executor);
}
