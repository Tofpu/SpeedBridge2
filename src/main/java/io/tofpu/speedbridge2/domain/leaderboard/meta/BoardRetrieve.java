package io.tofpu.speedbridge2.domain.leaderboard.meta;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface BoardRetrieve<T> {
    T retrieve(final @NotNull UUID uniqueId);
}
