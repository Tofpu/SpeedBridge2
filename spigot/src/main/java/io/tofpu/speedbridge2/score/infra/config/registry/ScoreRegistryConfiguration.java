package io.tofpu.speedbridge2.score.infra.config.registry;

import space.arim.dazzleconf.annote.ConfDefault;

public interface ScoreRegistryConfiguration {
    @ConfDefault.DefaultInteger(3)
    int entriesLimit();
}
