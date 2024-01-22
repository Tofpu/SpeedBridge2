package com.github.tofpu.speedbridge2.common.bridge.game;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.gameextra.land.arena.BasicArenaManager;
import com.github.tofpu.speedbridge2.object.Vector;

public class IslandArenaManager extends BasicArenaManager {
    // todo: make this option configurable
    private static final int ISLAND_GAP = 10;

    public IslandArenaManager(PlatformArenaAdapter arenaAdapter) {
        super(arenaAdapter, new Vector(0, 100, 0));
    }

    @Override
    protected int gapBetweenLand() {
        return ISLAND_GAP;
    }
}
