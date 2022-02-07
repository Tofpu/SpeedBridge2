package io.tofpu.speedbridge2.domain.common.config;

import io.tofpu.speedbridge2.domain.common.config.category.BlockMenuCategory;
import io.tofpu.speedbridge2.domain.common.config.category.GeneralCategory;
import io.tofpu.speedbridge2.domain.common.config.category.LeaderboardCategory;
import io.tofpu.speedbridge2.domain.common.config.category.LobbyCategory;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class PluginConfiguration {
    @Setting
    private final GeneralCategory generalCategory = new GeneralCategory();

    @Setting
    private final BlockMenuCategory blockMenuCategory = new BlockMenuCategory();

    @Setting
    private final LeaderboardCategory leaderboardCategory = new LeaderboardCategory();

    @Setting
    private final LobbyCategory lobbyCategory = new LobbyCategory();

    public LobbyCategory getLobbyCategory() {
        return lobbyCategory;
    }

    public BlockMenuCategory getBlockMenuCategory() {
        return blockMenuCategory;
    }

    public LeaderboardCategory getLeaderboardCategory() {
        return leaderboardCategory;
    }

    public GeneralCategory getGeneralCategory() {
        return generalCategory;
    }
}
