package io.tofpu.speedbridge2.domain.player.misc.stat;

import io.tofpu.speedbridge2.domain.player.misc.stat.type.PlayerWinStat;

public enum PlayerStatType {
    TOTAL_WINS;

    public static PlayerStat create(final PlayerStatType playerStatType) {
        switch (playerStatType.name()){
            case "TOTAL_WINS":
                return new PlayerWinStat();
        }
        return null;
    }
}
