package io.tofpu.speedbridge2.island.domain;

import io.tofpu.speedbridge2.positioning.PositionOffset;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.util.Position;
import io.tofpu.speedbridge2.util.PositionOrientation;

public record Island(int slot, Schematic schematic, PositionOffset positionOffset) {
    public Island(int slot, Schematic schematic, PositionOrientation position) {
        this(slot, schematic, new PositionOffset(position));
    }

    public PositionOrientation toWorldPosition(Position origin) {
        return positionOffset.applyTo(origin);
    }
}
