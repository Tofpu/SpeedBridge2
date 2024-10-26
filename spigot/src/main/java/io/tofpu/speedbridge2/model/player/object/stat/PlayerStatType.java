package io.tofpu.speedbridge2.model.player.object.stat;

import io.tofpu.speedbridge2.model.player.object.stat.type.SimplePlayerStat;

import java.util.Locale;
import java.util.UUID;

public enum PlayerStatType {
    TOTAL_WINS, TOTAL_TRIES;

    public static PlayerStat create(final UUID owner, final PlayerStatType playerStatType) {
        return create(owner, playerStatType, "0");
    }

    public static PlayerStat create(final UUID owner, final PlayerStatType playerStatType, final String value) {
        switch (playerStatType.name()) {
            case "TOTAL_WINS":
            case "TOTAL_TRIES":
                return new SimplePlayerStat(playerStatType.name()
                        .toLowerCase(Locale.ENGLISH), owner, Integer.parseInt(value));
        }
        return null;
    }

    public static PlayerStatType match(final String statType) {
        switch (statType.toUpperCase(Locale.ENGLISH)) {
            case "TOTAL_WINS":
                return PlayerStatType.TOTAL_WINS;
            case "TOTAL_TRIES":
                return PlayerStatType.TOTAL_TRIES;
        }
        return null;
    }
}
