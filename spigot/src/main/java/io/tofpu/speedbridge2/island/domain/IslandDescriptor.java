package io.tofpu.speedbridge2.island.domain;

import io.tofpu.speedbridge2.util.PositionOrientation;

import java.util.UUID;

public interface IslandDescriptor {
    int slot();
    UUID groupId();
    String schematicName();
    PositionOrientation position();

    static IslandDescriptor from(IslandDescriptor descriptor) {
        return Impl.from(descriptor);
    }

    record Impl(
       int slot,
       UUID groupId,
       String schematicName,
       PositionOrientation position
    ) implements IslandDescriptor {
        public static Impl from(IslandDescriptor descriptor) {
            return new Impl(
                    descriptor.slot(),
                    descriptor.groupId(),
                    descriptor.schematicName(),
                    descriptor.position()
            );
        }
    }
}
