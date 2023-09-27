package com.github.tofpu.speedbridge2.bridge.core.arena;

import com.github.tofpu.speedbridge2.bridge.IslandSchematic;
import com.github.tofpu.speedbridge2.bridge.Land;
import com.github.tofpu.speedbridge2.object.World;

public interface ArenaManager {
    Land generate(final IslandSchematic islandSchematic, World world);
    void unlock(final Land land);
    void clear(Land land);
}
