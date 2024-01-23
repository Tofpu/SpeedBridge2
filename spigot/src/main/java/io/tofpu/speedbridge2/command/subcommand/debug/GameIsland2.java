package io.tofpu.speedbridge2.command.subcommand.debug;

import io.tofpu.speedbridge2.model.island.arena.ArenaManager;
import io.tofpu.speedbridge2.model.island.object.GameIsland;
import io.tofpu.speedbridge2.model.island.object.land.IslandLand;

public class GameIsland2 extends GameIsland {
    private IslandLand islandLand;

    public GameIsland2(final ArenaManager arenaManager, GameIsland gameIsland) {
        super(arenaManager, gameIsland.getIsland(), gameIsland.getGamePlayer());
    }

    public void setIslandPlot(final IslandLand islandLand) {
        this.islandLand = islandLand;
    }

    @Override
    public IslandLand getIslandPlot() {
        return this.islandLand;
    }
}
