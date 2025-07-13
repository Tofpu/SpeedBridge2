package io.tofpu.speedbridge2.group.persistance;

import java.util.UUID;

public record GroupEntity(
        UUID id,
        String name
) {
}
