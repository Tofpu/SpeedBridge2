package com.github.tofpu.speedbridge2.common.bridge.setup;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.game.land.arena.BasicArenaManager;
import com.github.tofpu.speedbridge2.object.Vector;

public class SetupArenaManager extends BasicArenaManager {
    public SetupArenaManager(PlatformArenaAdapter arenaAdapter) {
        super(arenaAdapter, new Vector(0, 100, 100));
    }
}
