package io.tofpu.speedbridge2.game.config;

import io.tofpu.speedbridge2.game.config.arena.GameArenaConfiguration;
import io.tofpu.speedbridge2.game.config.experience.GamePlayerExperienceConfiguration;
import io.tofpu.speedbridge2.game.config.item.GameItemConfiguration;
import space.arim.dazzleconf.annote.SubSection;

public interface GameConfiguration {
    @SubSection
    GameArenaConfiguration arena();

    @SubSection
    GameItemConfiguration item();

    @SubSection
    GamePlayerExperienceConfiguration experience();
}
