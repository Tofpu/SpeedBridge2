package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.game.land.Land;
import com.github.tofpu.speedbridge2.common.game.land.arena.IslandSchematic;
import com.github.tofpu.speedbridge2.object.World;

public interface ArenaManager {
    Land generate(final IslandSchematic islandSchematic, World world);
    void unlock(final Land land);
    void clear(Land land);
}
