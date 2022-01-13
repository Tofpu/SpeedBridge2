package io.tofpu.speedbridge2.domain.game;

import io.tofpu.speedbridge2.domain.Island;
import io.tofpu.speedbridge2.domain.SchematicGeneration;

public final class GameIsland {
    private final Island island;
    private final GamePlayer gamePlayer;

    public GameIsland(final Island island, final GamePlayer gamePlayer) {
        this.island = island;
        this.gamePlayer = gamePlayer;

        SchematicGeneration.INSTANCE.reservePlot(this);
    }

    public Island getIsland() {
        return island;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
}
