package io.tofpu.speedbridge2.island.service;

import io.tofpu.speedbridge2.island.domain.Island;

import java.util.*;

public class IslandService {
    private final Map<Integer, Island> islandMap = new HashMap<>();

    public void registerIsland(Island island) {
        islandMap.put(island.slot(), island);
    }

    public Island getIsland(int slot) {
        return islandMap.get(slot);
    }

    public void removeIsland(int slot) {
        islandMap.remove(slot);
    }

    public Collection<Island> islands() {
        return Collections.unmodifiableCollection(islandMap.values());
    }
}
