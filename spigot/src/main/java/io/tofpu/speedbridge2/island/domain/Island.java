package io.tofpu.speedbridge2.island.domain;

import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.positioning.PositionOffset;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.util.Position;
import io.tofpu.speedbridge2.util.PositionOrientation;

public record Island(int slot, Group group, Schematic schematic, PositionOffset positionOffset) {
    public Island(int slot, Group group, Schematic schematic, PositionOrientation position) {
        this(slot, group, schematic, new PositionOffset(position));
    }

    public PositionOrientation toWorldPosition(Position origin) {
        return positionOffset.applyTo(origin);
    }
}
