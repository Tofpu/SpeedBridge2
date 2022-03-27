package io.tofpu.speedbridge2.model.island.plot;

import io.tofpu.speedbridge2.model.island.object.extra.GameIsland;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlotState{");
        sb.append("gameIsland=")
                .append(gameIsland);
        sb.append('}');
        return sb.toString();
    }
}
