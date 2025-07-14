package io.tofpu.speedbridge2.score.format.rounding;

import io.tofpu.speedbridge2.score.format.rounding.domain.RoundingSettings;
import io.tofpu.speedbridge2.score.format.rounding.domain.RoundingStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ScoreRoundingHandler {
    private final RoundingSettings settings;
    private final RoundingStrategy roundingStrategy;

    public ScoreRoundingHandler(RoundingSettings settings) {
        this.settings = settings;
        this.roundingStrategy = RoundingStrategy.from(settings.mode());
    }

    public double round(double number) {
        return roundingStrategy.format(number, settings.precision());
    }

    public BigDecimal formatDuration(double number) {
        RoundingMode mode = settings.mode() == io.tofpu.speedbridge2.score.format.rounding.domain.RoundingMode.NEAREST
                ? RoundingMode.HALF_UP
                : RoundingMode.DOWN;
        return new BigDecimal(number)
                .setScale(settings.decimalPoints(), mode);
    }
}
