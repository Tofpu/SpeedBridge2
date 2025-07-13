package io.tofpu.speedbridge2.island.persistence;

import java.util.UUID;

public record IslandEntity(
        int slot,
        UUID groupId,
        String schematicName,
        LocationEntity location
) {
    public record LocationEntity(
            double x, double y, double z,
            float yaw, float pitch
    ) {
    }
}
