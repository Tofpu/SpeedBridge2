package io.tofpu.speedbridge2.domain.common.config;

import io.tofpu.speedbridge2.domain.common.config.category.GeneralCategory;
import io.tofpu.speedbridge2.domain.common.config.category.LobbyCategory;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class PluginConfiguration {
    @Setting
    private final GeneralCategory generalCategory = new GeneralCategory();

    @Setting
    private final LobbyCategory lobbyCategory = new LobbyCategory();

    public LobbyCategory getLobbyCategory() {
        return lobbyCategory;
    }

    public GeneralCategory getGeneralCategory() {
        return generalCategory;
    }
}
