package io.tofpu.speedbridge2.island.persistence;

import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.island.domain.IslandDescriptor;
import io.tofpu.speedbridge2.island.domain.UnresolvedIsland;
import io.tofpu.speedbridge2.island.domain.UnresolvedReason;
import io.tofpu.speedbridge2.island.domain.ValidatableIsland;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.util.PositionOrientation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class IslandMapper {
    public IslandEntity toEntity(Island island) {
        return new IslandEntity(
                island.slot(),
                island.group().id(),
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

    public ValidatableIsland toDomain(IslandEntity entity, Group group, Schematic schematic) {
        Collection<UnresolvedReason> unresolvedReasonList = new ArrayList<>();
        if (group == null) {
            unresolvedReasonList.add(UnresolvedReason.MISSING_GROUP);
        }

        if (unresolvedReasonList.isEmpty()) {
            PositionOrientation positionOrientation = toDomain(entity.location());
            return new Island(
                    entity.slot(),
                    group,
                    schematic,
                    positionOrientation
            );
        }
        UnresolvedReason[] array = unresolvedReasonList.toArray(UnresolvedReason.EMPTY);
        IslandDescriptor.Impl target = new IslandDescriptor.Impl(
                entity.slot(), entity.groupId(), entity.schematicName(), toDomain(entity.location())
        );
        return new UnresolvedIsland(array, target);
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
