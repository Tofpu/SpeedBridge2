package com.github.tofpu.speedbridge2.common.gameextra;

import com.github.tofpu.speedbridge2.common.gameextra.land.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.arena.IslandSchematic;
import com.github.tofpu.speedbridge2.object.World;

public interface ArenaManager {
    Land generate(final IslandSchematic islandSchematic, World world);
    void unlock(final Land land);
    void clear(Land land);
}
