package io.tofpu.speedbridge2.island;

import io.tofpu.speedbridge2.schematic.Schematic;
import io.tofpu.speedbridge2.util.PositionOrientation;

public class Island {
    private final int slot;
    private final Schematic schematic;
    private final PositionOrientation absoluteLocation;

    public Island(int slot, Schematic schematic, PositionOrientation absoluteLocation) {
        this.slot = slot;
        this.schematic = schematic;
        this.absoluteLocation = absoluteLocation;
    }

    public int slot() {
        return slot;
    }

    public Schematic schematic() {
        return schematic;
    }

    public PositionOrientation positionOrientation() {
        return absoluteLocation;
    }
}
