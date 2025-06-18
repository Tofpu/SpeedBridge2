package io.tofpu.speedbridge2.score.infra.config.format;

import io.tofpu.speedbridge2.score.format.rounding.domain.RoundingMode;
import space.arim.dazzleconf.annote.SubSection;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultBoolean;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultDouble;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultInteger;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultString;

public interface ScoreFormatConfiguration {

    @SubSection
    Rounding rounding();

    interface Rounding {
        @DefaultBoolean(true)
        boolean enabled();

        @DefaultString("DOWN")
        RoundingMode mode();

        @DefaultDouble(0.005)
        double precision();

        @DefaultInteger(3)
        int decimalPlaces();
    }
}
