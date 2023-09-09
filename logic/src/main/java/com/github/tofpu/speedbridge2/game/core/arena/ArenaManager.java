package com.github.tofpu.speedbridge2.game.core.arena;

import com.github.tofpu.speedbridge2.game.island.Island;
import com.github.tofpu.speedbridge2.game.island.arena.Land;

public interface ArenaManager {
    Land generate(final Island island);
    void unlock(final Land land);
}
