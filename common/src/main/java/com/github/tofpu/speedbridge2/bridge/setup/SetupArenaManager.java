package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.game.land.arena.BasicArenaManager;
import com.github.tofpu.speedbridge2.object.Vector;

public class SetupArenaManager extends BasicArenaManager {
    public SetupArenaManager(ArenaAdapter arenaAdapter) {
        super(arenaAdapter, new Vector(0, 100, 100));
    }
}
