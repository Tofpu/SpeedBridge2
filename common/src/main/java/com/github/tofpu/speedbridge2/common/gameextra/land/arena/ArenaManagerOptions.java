package com.github.tofpu.speedbridge2.common.gameextra.land.arena;

import com.github.tofpu.speedbridge2.object.Vector;

public class ArenaManagerOptions {
    private final Vector initialPosition;
    private final int gapBetweenIsland;

    public ArenaManagerOptions(Vector initialPosition, int gapBetweenIsland) {
        this.initialPosition = initialPosition;
        this.gapBetweenIsland = gapBetweenIsland;
    }

    public ArenaManagerOptions(Vector initialPosition) {
        this(initialPosition, 10);
    }

    public Vector initialPosition() {
        return initialPosition;
    }

    public int gapBetweenIsland() {
        return gapBetweenIsland;
    }
}
