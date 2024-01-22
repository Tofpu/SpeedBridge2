package com.github.tofpu.speedbridge2.common.gameextra.land.arena;

import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.common.schematic.Schematic;

public class IslandSchematic extends Schematic {
    private final int slot;
    private final Location absolutePosition;

    public IslandSchematic(int slot, Schematic schematic, Location absolutePosition) {
        super(schematic.schematicFile(), schematic.originPosition());
        this.slot = slot;
        this.absolutePosition = absolutePosition;
    }

    public IslandSchematic(int slot, Schematic schematic, World world) {
        this(slot, schematic, schematic.originPosition().toLocation(world));
    }

    public Location absolutePosition() {
        return absolutePosition;
    }

    public int slot() {
        return slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IslandSchematic that = (IslandSchematic) o;

        return slot == that.slot;
    }

    @Override
    public int hashCode() {
        return slot;
    }
}
