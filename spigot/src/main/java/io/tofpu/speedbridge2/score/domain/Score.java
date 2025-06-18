package io.tofpu.speedbridge2.score.domain;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public record Score(
        UUID playerId,
        int slot,
        double time,
        Instant timestamp
) implements Comparable<Score> {
    @Override
    public int compareTo(@NotNull Score o) {
        int slotComparison = Integer.compare(slot, o.slot);
        if (slotComparison != 0) {
            return slotComparison;
        }
        return Double.compare(time, o.time);
    }
}
