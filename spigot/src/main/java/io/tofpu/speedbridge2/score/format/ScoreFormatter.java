package io.tofpu.speedbridge2.score.format;

import io.tofpu.speedbridge2.score.domain.Score;
import io.tofpu.speedbridge2.score.format.rounding.ScoreRoundingHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ScoreFormatter {
    private final ScoreRoundingHandler roundingHandler;

    public ScoreFormatter(ScoreRoundingHandler roundingHandler) {
        this.roundingHandler = roundingHandler;
    }

    public String format(Score score) {
        return format(score.time());
    }

    public String format(double time) {
        double roundedTime = roundingHandler.round(time);
        return formatDuration(roundingHandler.formatDuration(roundedTime));
    }

    String formatDuration(BigDecimal decimal) {
        double value = decimal.doubleValue();
        if (value < 60) {
            return value + "s";
        } else if (value < 3600) {
            return decimal.divide(BigDecimal.valueOf(60), RoundingMode.DOWN).toPlainString() + "m";
        } else if (value < 86400) {
            return decimal.divide(BigDecimal.valueOf(3600), RoundingMode.DOWN) + "h";
        } else {
            return decimal.divide(BigDecimal.valueOf(86400), RoundingMode.DOWN) + "d";
        }
    }
}
