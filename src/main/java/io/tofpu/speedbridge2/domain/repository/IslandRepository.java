package io.tofpu.speedbridge2.domain.repository;

import io.tofpu.speedbridge2.database.Databases;
import io.tofpu.speedbridge2.domain.Island;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IslandRepository {

    public Map<Integer, Island> loadIslands() {
        final List<Island> islands = Databases.ISLAND_DATABASE.getStoredIslands();

        System.out.println("loaded islands:");
        System.out.println(islands);

        final Map<Integer, Island> islandMap = new HashMap<>();
        if (!islands.isEmpty()) {
            for (final Island island : islands) {
                islandMap.put(island.getSlot(), island);
            }
        }

        return islandMap;
    }

}
