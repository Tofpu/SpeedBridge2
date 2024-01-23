package com.github.tofpu.speedbridge2.common.game.score.object;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Locale;
import java.util.UUID;

@Entity(name = "scores")
@Data
public class Score implements Comparable<Score> {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "VARCHAR(36)", updatable = false)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(columnDefinition = "VARCHAR(36)", updatable = false)
    @Type(type = "uuid-char")
    private final UUID playerId;
    private final int islandSlot;
    private final double nanoseconds;

    public static Score inSeconds(UUID playerId, int islandSlot, double seconds) {
        return new Score(playerId, islandSlot, seconds * 1_000_000_000.0);
    }

    public static Score inNanoSeconds(UUID playerId, int islandSlot, double nanoseconds) {
        return new Score(playerId, islandSlot, nanoseconds);
    }

    private Score(UUID playerId, int islandSlot, double nanoseconds) {
        this.playerId = playerId;
        this.islandSlot = islandSlot;
        this.nanoseconds = nanoseconds;
    }

    private Score() {
        this(null, -1, -1);
    }

    public double nanoseconds() {
        return nanoseconds;
    }

    public double seconds() {
        return nanoseconds / 1_000_000_000.0;
    }

    public String textSeconds() {
        double seconds = seconds();
        return String.format(Locale.US, "%.3f", seconds);
    }

    @Override
    public int compareTo(@NotNull Score o) {
        return Double.compare(this.nanoseconds, o.nanoseconds);
    }
}
