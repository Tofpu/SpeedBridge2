package io.tofpu.speedbridge2.model.island.object;

import io.tofpu.speedbridge2.model.island.IslandService;
import io.tofpu.speedbridge2.model.island.arena.ArenaManager;

public class IslandBuild extends Island {
    public IslandBuild(final IslandService islandService,
            final ArenaManager arenaManager, final int slot,
            final String category) {
        super(islandService, arenaManager, slot, category);
    }

    @Override
    public boolean isReady() {
        // return false since this is a creation island
        return false;
    }

    @Override
    public void updateLeaderboard() {
        // do nothing here since this is a creation island
    }

    @Override
    public void delete() {
        // do nothing here since this is a creation island
    }

    @Override
    protected void update() {
        // do nothing here since this is a creation island
    }

    public Island toRegularIsland() {
        final Island island = new Island(islandService, super.arenaManager, this.getSlot(),
                        this.getCategory());
        island.selectSchematic(getSchematicName());
        island.setAbsoluteLocation(getAbsoluteLocation());
        return island;
    }

    @Override
    public String toString() {
        return "IslandCreation: " + this.getSlot() + " " + this.getCategory() + " " + getSchematicName();
    }
}
