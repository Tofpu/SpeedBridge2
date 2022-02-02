package io.tofpu.speedbridge2.domain.island;

import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.IslandBoard;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class IslandHandler {
    private final Map<Integer, Island> islands = new HashMap<>();

    public void load(final Map<Integer, Island> loadedIslands) {
        for (final Island island : loadedIslands.values()) {
            IslandBoard.add(island);
        }
        this.islands.putAll(loadedIslands);
    }

    public IslandCreationResult createIsland(final int slot, final String category) {
        return createIsland(slot, category, "");
    }

    public IslandCreationResult createIsland(final int slot, final String category,
            final String schematic) {
        final Island island = new Island(slot, category);

        // if the schematic is not empty, and it doesn't exist, return UNKNOWN_SCHEMATIC!
        if (!schematic.isEmpty() && !island.selectSchematic(schematic)) {
            return IslandCreationResult.UNKNOWN_SCHEMATIC;
        }
        final Island previousIsland = this.islands.putIfAbsent(slot, island);

        // if the island didn't exist beforehand, insert the object
        if (previousIsland == null) {
            Databases.ISLAND_DATABASE.insert(island);
            IslandBoard.add(island);

            return IslandCreationResult.SUCCESS;
        } else {
            // otherwise, return ISLAND_ALREADY_EXISTS
            return IslandCreationResult.ISLAND_ALREADY_EXISTS;
        }
    }

    public Island findIslandBy(final int slot) {
        return this.islands.get(slot);
    }

    public Island findIslandBy(final String category) {
        for (final Island island : this.islands.values()) {
            if (island.getCategory().equals(category)) {
                return island;
            }
        }
        return null;
    }

    public Island deleteIsland(final int slot) {
        final Island island = this.islands.remove(slot);

        // if the island is not null, wipe said island & return deleted island!
        if (island != null) {
            Databases.ISLAND_DATABASE.delete(slot);
            IslandBoard.remove(island);

            return island;
        }
        return null;
    }

    public Collection<Island> getIslands() {
        return Collections.unmodifiableCollection(this.islands.values());
    }

    public enum IslandCreationResult {
        UNKNOWN_SCHEMATIC, ISLAND_ALREADY_EXISTS, SUCCESS
    }
}
