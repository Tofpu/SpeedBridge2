package io.tofpu.speedbridge2.island.domain;

import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.util.PositionOrientation;

public record Island(int slot, Schematic schematic, PositionOrientation spawnPoint) {
    }
}
