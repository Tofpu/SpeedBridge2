package io.tofpu.speedbridge2.game.arena;

import io.tofpu.speedbridge2.game.IslandGameSession;
import io.tofpu.speedbridge2.game.arena.land.LandArea;
import io.tofpu.speedbridge2.game.arena.land.LandSchematic;

import java.util.UUID;

public interface GameArenaManager {
    LandArea reserveArea(IslandGameSession slot, LandSchematic schematic);

    void unreserveArea(UUID id);

    boolean isReserved(UUID id);
}
