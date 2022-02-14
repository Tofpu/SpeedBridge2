package io.tofpu.speedbridge2.domain.common.database;

import io.tofpu.speedbridge2.domain.island.IslandDatabase;
import io.tofpu.speedbridge2.domain.player.PlayerDatabase;
import io.tofpu.speedbridge2.domain.player.misc.block.BlockDatabase;
import io.tofpu.speedbridge2.domain.player.misc.score.ScoreDatabase;
import io.tofpu.speedbridge2.domain.player.misc.stat.StatsDatabase;
import org.jetbrains.annotations.NotNull;

public final class Databases {
    public static final @NotNull IslandDatabase ISLAND_DATABASE;
    public static final @NotNull PlayerDatabase PLAYER_DATABASE;
    public static final @NotNull StatsDatabase STATS_DATABASE;
    public static final @NotNull ScoreDatabase SCORE_DATABASE;
    public static final @NotNull BlockDatabase BLOCK_DATABASE;

    static {
        ISLAND_DATABASE = new IslandDatabase();
        PLAYER_DATABASE = new PlayerDatabase();
        STATS_DATABASE = new StatsDatabase();
        SCORE_DATABASE = new ScoreDatabase();
        BLOCK_DATABASE = new BlockDatabase();
    }
}
