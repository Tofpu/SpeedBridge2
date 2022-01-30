package io.tofpu.speedbridge2.domain.island;

import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.common.util.BridgeUtil;
import io.tofpu.speedbridge2.domain.island.object.Island;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.tofpu.speedbridge2.domain.common.util.DatabaseUtil.runAsync;

public final class IslandRepository {
    public CompletableFuture<Map<Integer, Island>> loadIslands() {
        return runAsync(() -> {
            final List<Island> islands = new ArrayList<>();
            try {
                islands.addAll(Databases.ISLAND_DATABASE.getStoredIslands().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            BridgeUtil.debug("loaded islands:");
            BridgeUtil.debug(String.valueOf(islands));

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
