package io.tofpu.speedbridge2.model.island;

import io.tofpu.speedbridge2.model.common.database.Databases;
import io.tofpu.speedbridge2.model.island.object.Island;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class IslandRepository {
    public @NotNull CompletableFuture<Map<Integer, Island>> getIslandsAsync() {
        return Databases.ISLAND_DATABASE.getStoredIslandsAsync().thenApply((islands) -> {
            final Map<Integer, Island> islandMap = new HashMap<>();
            for (final Island island : islands) {
                islandMap.put(island.getSlot(), island);
            }

            return islandMap;
        });
    }
}
