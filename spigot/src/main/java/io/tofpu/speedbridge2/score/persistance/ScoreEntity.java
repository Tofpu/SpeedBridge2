package io.tofpu.speedbridge2.score.persistance;

import java.time.Instant;
import java.util.UUID;

public record ScoreEntity(
        UUID playerId,
        int slot,
        double time,
        Instant timestamp
) {
}
