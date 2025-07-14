package io.tofpu.speedbridge2.island.domain;

import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.positioning.PositionOffset;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.util.Position;
import io.tofpu.speedbridge2.util.PositionOrientation;

import java.util.UUID;

public record Island(int slot, Group group, Schematic schematic, PositionOffset positionOffset) implements ValidatableIsland {
    public Island(int slot, Group group, Schematic schematic, PositionOrientation position) {
        this(slot, group, schematic, new PositionOffset(position));
    }

    public PositionOrientation toWorldPosition(Position origin) {
        return positionOffset.applyTo(origin);
    }

    @Override
    public UUID groupId() {
        return group.id();
    }

    @Override
    public String schematicName() {
        return schematic.name();
    }

    @Override
    public PositionOrientation position() {
        return positionOffset.offset();
    }

    @Override
    public UnresolvedReason[] invalidReasons() {
        return UnresolvedReason.EMPTY;
    }
}
