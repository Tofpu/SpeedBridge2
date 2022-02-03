package io.tofpu.speedbridge2.domain.island.plot;

import io.tofpu.speedbridge2.domain.island.object.extra.GameIsland;

public final class PlotState {
    private GameIsland gameIsland;

    public void reservePlotWith(final GameIsland gameIsland) {
        this.gameIsland = gameIsland;
    }

    public void freePlot() {
        gameIsland = null;
    }

    public boolean isPlotFree() {
        return gameIsland == null;
    }

    public GameIsland getGameIsland() {
        return gameIsland;
    }
}
