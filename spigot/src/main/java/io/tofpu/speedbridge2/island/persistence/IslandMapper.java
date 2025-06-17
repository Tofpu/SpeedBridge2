package io.tofpu.speedbridge2.island.persistence;

import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.util.PositionOrientation;
import org.jetbrains.annotations.NotNull;

public class IslandMapper {
    public IslandEntity toEntity(Island island) {
        return new IslandEntity(
                island.slot(),
                island.schematic().name(),
                toEntity(island.positionOffset().offset())
        );
    }

    private static IslandEntity.@NotNull LocationEntity toEntity(PositionOrientation position) {
        return new IslandEntity.LocationEntity(
                position.getX(),
                position.getY(),
                position.getZ(),
                position.getYaw(),
                position.getPitch()
        );
    }

    public Island toDomain(IslandEntity entity, Schematic schematic) {
        PositionOrientation positionOrientation = toDomain(entity.location());
        return new Island(
                entity.slot(),
                schematic,
                positionOrientation
        );
    }

    private static @NotNull PositionOrientation toDomain(IslandEntity.LocationEntity locationEntity) {
        return new PositionOrientation(
                locationEntity.x(),
                locationEntity.y(),
                locationEntity.z(),
                locationEntity.yaw(),
                locationEntity.pitch()
        );
    }
}
