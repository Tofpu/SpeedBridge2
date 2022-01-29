package io.tofpu.speedbridge2.domain.player.misc.stat.type;

import io.tofpu.speedbridge2.domain.player.misc.stat.PlayerStat;

public class PlayerWinStat implements PlayerStat {
    private int totalWins = 0;

    public PlayerWinStat() {}

    public PlayerWinStat(final int totalWins) {
        this.totalWins = totalWins;
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
