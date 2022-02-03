package io.tofpu.speedbridge2.domain.common.config.category;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class LeaderboardCategory {

    @Setting("update-interval")
    @Comment("A leaderboard shall update on every x interval. It's defined in seconds.")
    private int updateInterval = 60;

    @Setting("leaderboard-format")
    @Comment("The leaderboard's format.")
    private String leaderboardFormat = "&e%position%. &6%name% &7(%score%)";

    public int getUpdateInterval() {
        return updateInterval;
    }

    public String getLeaderboardFormat() {
        return leaderboardFormat;
    }
}
