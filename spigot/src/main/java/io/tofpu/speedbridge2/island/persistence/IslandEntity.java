package io.tofpu.speedbridge2.island.persistence;

public record IslandEntity(
        int slot, String schematicName,
        LocationEntity location
) {
    public record LocationEntity(
            double x, double y, double z,
            float yaw, float pitch
    ) {
    }
}
