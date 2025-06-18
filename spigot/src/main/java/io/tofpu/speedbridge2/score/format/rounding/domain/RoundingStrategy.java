package io.tofpu.speedbridge2.score.format.rounding.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface RoundingStrategy {
    static RoundingStrategy from(io.tofpu.speedbridge2.score.format.rounding.domain.RoundingMode mode) {
        switch (mode) {
            case NEAREST -> {
                return new Nearest();
            }
            case DOWN ->  {
                return new Down();
            }
            default -> {
                return noop();
            }
        }
    }

    double format(double input, double precision);

    static RoundingStrategy noop() {
        return (input, precision) -> input;
    }

    class Down implements RoundingStrategy {
        @Override
        public double format(double input, double precision) {
            return Math.floor(input / precision) * precision;
        }
    }

    class Nearest implements RoundingStrategy {
        @Override
        public double format(double input, double precision) {
            BigDecimal bigInput = new BigDecimal(input + "");
            BigDecimal bigPrecision = new BigDecimal(precision + "");
            BigDecimal divided = bigInput.divide(bigPrecision, 0, RoundingMode.HALF_UP);
            return divided.multiply(bigPrecision).doubleValue();
        }
    }
}
