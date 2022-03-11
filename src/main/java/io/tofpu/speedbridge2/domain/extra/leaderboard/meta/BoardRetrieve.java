package io.tofpu.speedbridge2.domain.extra.leaderboard.meta;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface BoardRetrieve<T> {
  T retrieve(final @NotNull UUID uniqueId);
}
