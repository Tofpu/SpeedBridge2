package io.tofpu.speedbridge2.domain.handler;

import io.tofpu.speedbridge2.database.Databases;
import io.tofpu.speedbridge2.database.IslandDatabase;
import io.tofpu.speedbridge2.domain.Island;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class IslandHandler {
    private final Map<Integer, Island> islands = new HashMap<>();

    public void load(final Map<Integer, Island> loadedIslands) {
        this.islands.putAll(loadedIslands);
    }

    public Island createIsland(final int slot) {
        final Island island = new Island(slot, "default");

        if (this.islands.putIfAbsent(slot, island) == null) {
            Databases.ISLAND_DATABASE.insert(island);
        }
        return island;
    }

    public Island deleteIsland(final int slot) {
        final Island island = this.islands.remove(slot);

        if (island != null) {
            Databases.ISLAND_DATABASE.delete(slot);
        }

        return island;
    }

    public Collection<Island> getIslands() {
        return new ArrayList<>(this.islands.values());
    }
}
