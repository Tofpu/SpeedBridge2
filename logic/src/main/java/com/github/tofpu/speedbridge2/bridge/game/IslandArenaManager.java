package com.github.tofpu.speedbridge2.bridge.game;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.bridge.BasicArenaManager;
import com.github.tofpu.speedbridge2.object.Vector;

public class IslandArenaManager extends BasicArenaManager {
    // todo: make this option configurable
    private static final int ISLAND_GAP = 10;

    public IslandArenaManager(ArenaAdapter arenaAdapter) {
        super(arenaAdapter, new Vector(0, 100, 0));
    }

    @Override
    protected int gapBetweenLand() {
        return ISLAND_GAP;
    }
}
