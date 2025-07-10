package io.tofpu.speedbridge2.game.infra.config;

import io.tofpu.speedbridge2.game.infra.config.arena.GameArenaConfiguration;
import io.tofpu.speedbridge2.game.infra.config.experience.GamePlayerExperienceConfiguration;
import io.tofpu.speedbridge2.game.infra.config.item.GameHotbarConfiguration;
import space.arim.dazzleconf.annote.SubSection;

public interface GameConfiguration {
    @SubSection
    GameArenaConfiguration arena();

    @SubSection
    GameHotbarConfiguration hotbar();

    @SubSection
    GamePlayerExperienceConfiguration experience();
}
