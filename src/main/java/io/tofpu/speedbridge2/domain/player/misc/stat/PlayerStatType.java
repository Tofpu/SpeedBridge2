package io.tofpu.speedbridge2.domain.player.misc.stat;

import io.tofpu.speedbridge2.domain.player.misc.stat.type.PlayerWinStat;

import java.util.Locale;
import java.util.UUID;

public enum PlayerStatType {
    TOTAL_WINS;

    public static PlayerStat create(final UUID owner,
            final PlayerStatType playerStatType) {
        return create(owner, playerStatType, "0");
    }

    public static PlayerStat create(final UUID owner,
            final PlayerStatType playerStatType, final String value) {
        switch (playerStatType.name()){
            case "TOTAL_WINS":
                return new PlayerWinStat(owner, Integer.parseInt(value));
        }
        return null;
    }

    public static PlayerStatType match(final String statType) {
        switch (statType.toUpperCase(Locale.ENGLISH)){
            case "TOTAL_WINS":
                return PlayerStatType.TOTAL_WINS;
        }
        return null;
    }
}
