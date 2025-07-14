package io.tofpu.speedbridge2;

import io.tofpu.speedbridge2.arena.DefaultPositionCalculator;
import io.tofpu.speedbridge2.arena.PositionCalculator;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Constants {
    interface ArenaPositioning {
        PositionCalculator<Integer> SETUP = new DefaultPositionCalculator<>(100_000, 0, 100, () -> 0);
        Function<Supplier<Integer>, PositionCalculator<Integer>> GAME =
                xAxisGapSupplier -> new DefaultPositionCalculator<>(0, 0, 0, xAxisGapSupplier);
    }

    interface Placeholder {
        String PLACEHOLDER_PREFIX = "sb";
    }
}
