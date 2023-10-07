package com.github.tofpu.speedbridge2.bridge.score;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Entity(name = "scores")
@Data
public class Score implements Comparable<Score> {
    @Id
    @Column(columnDefinition = "VARCHAR(36)", updatable = false)
    @Type(type = "uuid-char")
    private final UUID playerId;
    private final int islandSlot;
    private final double timerInNano;

    public static Score inSeconds(UUID playerId, int islandSlot, double seconds) {
        return new Score(playerId, islandSlot, TimeUnit.SECONDS.toNanos((long) seconds));
    }

    public static Score inNano(UUID playerId, int islandSlot, double nano) {
        return new Score(playerId, islandSlot, nano);
    }

    private Score(UUID playerId, int islandSlot, double timerInNano) {
        this.playerId = playerId;
        this.islandSlot = islandSlot;
        this.timerInNano = timerInNano;
    }

    private Score() {
        this(null, -1, -1);
    }

    public double timerInSeconds() {
        return TimeUnit.NANOSECONDS.toSeconds((long) timerInNano);
    }

    @Override
    public int compareTo(@NotNull Score o) {
        return Double.compare(this.timerInNano, o.timerInNano);
    }
}
