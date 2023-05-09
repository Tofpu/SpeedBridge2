package io.tofpu.speedbridge2.island.fake;

import io.tofpu.speedbridge2.game.arena.land.LandSchematic;

public class FakeIsland {
    private final int slot;
    private final LandSchematic schematic;

    public FakeIsland(int slot, LandSchematic schematic) {
        this.slot = slot;
        this.schematic = schematic;
    }

    public int getSlot() {
        return slot;
    }

    public LandSchematic getSchematic() {
        return schematic;
    }
}
