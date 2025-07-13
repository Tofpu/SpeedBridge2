package io.tofpu.speedbridge2.setup.domain;

import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.schematic.domain.Schematic;

import javax.annotation.Nullable;

public record SetupInfo(int slot, @Nullable Group group, Schematic schematic) {
}
