package io.tofpu.speedbridge2.island.domain;

import io.tofpu.speedbridge2.util.PositionOrientation;

import java.util.UUID;

public record UnresolvedIsland(
        UnresolvedReason[] reasons,
        IslandDescriptor target
) implements ValidatableIsland {
    public UnresolvedIsland {
        if (reasons.length == 0) {
            throw new IllegalStateException("InvalidIsland must be constructed with at-least a single invalid type.");
        }
    }

    @Override
    public int slot() {
        return target.slot();
    }

    @Override
    public UUID groupId() {
        return target.groupId();
    }

    @Override
    public String schematicName() {
        return target.schematicName();
    }

    @Override
    public PositionOrientation position() {
        return target.position();
    }

    @Override
    public UnresolvedReason[] invalidReasons() {
        return reasons;
    }
}
