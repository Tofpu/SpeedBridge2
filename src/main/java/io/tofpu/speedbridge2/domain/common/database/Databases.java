package io.tofpu.speedbridge2.domain.common.database;

import io.tofpu.speedbridge2.domain.island.IslandDatabase;
import io.tofpu.speedbridge2.domain.player.PlayerDatabase;

public final class Databases {
    public static final IslandDatabase ISLAND_DATABASE;
    public static final PlayerDatabase PLAYER_DATABASE;

    static {
        ISLAND_DATABASE = new IslandDatabase();
        PLAYER_DATABASE = new PlayerDatabase();
    }
}
