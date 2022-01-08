package io.tofpu.speedbridge2.domain.repository;

import io.tofpu.speedbridge2.database.Databases;
import io.tofpu.speedbridge2.domain.Island;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.database.util.DatabaseUtil.runAsync;

public final class IslandRepository {

    public CompletableFuture<Map<Integer, Island>> loadIslands() {
        return runAsync(() -> {
            List<Island> islands;
            try {
                islands = Databases.ISLAND_DATABASE.getStoredIslands().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                islands = new ArrayList<>();
            }

            System.out.println("loaded islands:");
            System.out.println(islands);

            final Map<Integer, Island> islandMap = new HashMap<>();
            if (!islands.isEmpty()) {
                for (final Island island : islands) {
                    islandMap.put(island.getSlot(), island);
                }
            }

            return islandMap;
        });
    }
}
