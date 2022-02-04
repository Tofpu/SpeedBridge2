package io.tofpu.speedbridge2.domain.common.database;

import io.tofpu.speedbridge2.domain.island.IslandDatabase;
import io.tofpu.speedbridge2.domain.player.PlayerDatabase;
import io.tofpu.speedbridge2.domain.player.misc.score.ScoreDatabase;
import io.tofpu.speedbridge2.domain.player.misc.stat.StatsDatabase;

public final class Databases {
    public static final IslandDatabase ISLAND_DATABASE;
    public static final PlayerDatabase PLAYER_DATABASE;
    public static final StatsDatabase STATS_DATABASE;
    public static final ScoreDatabase SCORE_DATABASE;

    static {
        ISLAND_DATABASE = new IslandDatabase();
        PLAYER_DATABASE = new PlayerDatabase();
        STATS_DATABASE = new StatsDatabase();
        SCORE_DATABASE = new ScoreDatabase();
    }
}
