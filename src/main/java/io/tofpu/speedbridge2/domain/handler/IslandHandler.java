package io.tofpu.speedbridge2.domain.handler;

import io.tofpu.speedbridge2.domain.Island;

import java.util.HashMap;
import java.util.Map;

public class IslandHandler {
    private final Map<Integer, Island> islands = new HashMap<>();

    public Island createIsland(final int slot) {
        return this.islands.putIfAbsent(slot, new Island(slot));
    }
}
