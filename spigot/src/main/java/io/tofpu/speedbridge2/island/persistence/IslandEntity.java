package io.tofpu.speedbridge2.island.persistence;

public record IslandEntity(
        int slot, String schematicName,
        LocationEntity location
) {
    public record LocationEntity(
            int x, int y, int z,
            float yaw, float pitch
    ) {
    }
}
