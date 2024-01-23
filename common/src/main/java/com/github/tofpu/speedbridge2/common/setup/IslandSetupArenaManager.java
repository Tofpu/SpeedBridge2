package com.github.tofpu.speedbridge2.common.setup;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.gameextra.land.arena.BasicArenaManager;
import com.github.tofpu.speedbridge2.object.Vector;

public class IslandSetupArenaManager extends BasicArenaManager {
    public IslandSetupArenaManager(PlatformArenaAdapter arenaAdapter) {
        super(arenaAdapter, new Vector(0, 100, 100));
    }
}
