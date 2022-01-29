package io.tofpu.speedbridge2.domain.player.misc.stat.type;

import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStat;

import java.util.UUID;

public class PlayerWinStat implements PlayerStat {
    private final UUID owner;
    private int totalWins = 0;

    public PlayerWinStat(final UUID owner) {
        this.owner = owner;
    }

    public PlayerWinStat(final UUID owner, final int totalWins) {
        this(owner);
        this.totalWins = totalWins;
    }

    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public String getKey() {
        return "total_wins";
    }

    @Override
    public String getValue() {
        return totalWins + "";
    }

    @Override
    public void increment() {
        totalWins++;
        Databases.STATS_DATABASE.update(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlayerWinStat{");
        sb.append("totalWins=")
                .append(totalWins);
        sb.append('}');
        return sb.toString();
    }
}
