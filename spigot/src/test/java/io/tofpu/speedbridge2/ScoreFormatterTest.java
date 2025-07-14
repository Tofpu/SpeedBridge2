package io.tofpu.speedbridge2;

import io.tofpu.speedbridge2.score.format.ScoreFormatter;
import io.tofpu.speedbridge2.score.format.rounding.ScoreRoundingHandler;
import io.tofpu.speedbridge2.score.format.rounding.domain.RoundingMode;
import io.tofpu.speedbridge2.score.format.rounding.domain.RoundingSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreFormatterTest {
    @Test
    void givenNumberExceedsPrecision_whenRounded_thenNumberShouldBeRoundedToMatchPrecision() {
        RoundingSettings settings = new RoundingSettings(RoundingMode.NEAREST, 0.005, 3);
        ScoreRoundingHandler roundingHandler = new ScoreRoundingHandler(settings);
        ScoreFormatter scoreFormatter = new ScoreFormatter(roundingHandler);

        double input = 1.3333;
        double expected = 1.335;
        assertEquals(expected, roundingHandler.round(input));
        assertEquals(expected + "s", scoreFormatter.format(input));
    }

    @Test
    void givenNumberDoesNotExceedPrecision_whenRounded_thenNumberShouldBeLeftAsIs() {
        RoundingSettings settings = new RoundingSettings(RoundingMode.NEAREST, 0.005, 3);
        ScoreRoundingHandler roundingHandler = new ScoreRoundingHandler(settings);
        ScoreFormatter scoreFormatter = new ScoreFormatter(roundingHandler);

        double input = 1.33;
        double expected = 1.33;
        assertEquals(expected, roundingHandler.round(input));
        assertEquals(expected + "s", scoreFormatter.format(input));
    }
}
