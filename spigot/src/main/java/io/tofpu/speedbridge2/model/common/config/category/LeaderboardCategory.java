package io.tofpu.speedbridge2.model.common.config.category;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class LeaderboardCategory {

    @Setting("global-update-interval")
    @Comment("A leaderboard will update on every x interval. It's defined in seconds.")
    private int globalUpdateInterval = 60;

    @Setting("session-update-interval")
    @Comment("A session leaderboard will update on every x interval. It's defined in " +
            "seconds.")
    private int sessionUpdateInterval = 10;

    @Setting("leaderboard-format")
    @Comment("The leaderboard's format.")
    private String leaderboardFormat = "&e%position%. &6%name% &7(%score%)";

    public int getGlobalUpdateInterval() {
        return globalUpdateInterval;
    }

    public int getSessionUpdateInterval() {
        return sessionUpdateInterval;
    }

    public String getLeaderboardFormat() {
        return leaderboardFormat;
    }
}
