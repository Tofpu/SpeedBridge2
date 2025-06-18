package io.tofpu.speedbridge2.score.format.rounding.domain;

public record RoundingSettings(
        RoundingMode mode,
        double precision,
        int decimalPoints
) {
}
