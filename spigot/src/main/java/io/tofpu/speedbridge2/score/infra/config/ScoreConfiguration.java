package io.tofpu.speedbridge2.score.infra.config;

import io.tofpu.speedbridge2.score.infra.config.format.ScoreFormatConfiguration;
import io.tofpu.speedbridge2.score.infra.config.registry.ScoreRegistryConfiguration;
import space.arim.dazzleconf.annote.SubSection;

public interface ScoreConfiguration {
    @SubSection
    ScoreFormatConfiguration format();

    @SubSection
    ScoreRegistryConfiguration registry();
}
