package io.tofpu.speedbridge2.model.island.object.extra;

import io.tofpu.speedbridge2.model.island.object.Island;

public class IslandCreation extends Island {
    public IslandCreation(final int slot, final String category) {
        super(slot, category);
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
        final Island island = new Island(this.getSlot(), this.getCategory());
        island.selectSchematic(getSchematicName());
        island.setAbsoluteLocation(getAbsoluteLocation());
        return island;
    }

    @Override
    public String toString() {
        return "IslandCreation: " + this.getSlot() + " " + this.getCategory() + " " + getSchematicName();
    }
}
