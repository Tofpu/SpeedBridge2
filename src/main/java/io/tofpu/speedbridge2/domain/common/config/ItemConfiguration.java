package io.tofpu.speedbridge2.domain.common.config;

import io.tofpu.speedbridge2.domain.common.config.category.GameCategory;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class ItemConfiguration {
    @Setting("join-game")
    private final GameCategory gameCategory = new GameCategory();

    public GameCategory getGameCategory() {
        return gameCategory;
    }
}
