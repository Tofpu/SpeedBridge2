package io.tofpu.speedbridge2.domain.island;

import io.tofpu.speedbridge2.domain.common.database.Databases;
import io.tofpu.speedbridge2.domain.island.object.Island;
import io.tofpu.speedbridge2.domain.island.object.IslandBoard;
import io.tofpu.speedbridge2.domain.island.schematic.SchematicManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class IslandHandler {
    private final @NotNull Map<Integer, Island> islands = new HashMap<>();

    /**
     * Loads the islands from the map of islands
     *
     * @param loadedIslands A map of island ids to island objects.
     */
    public void load(final @NotNull Map<Integer, Island> loadedIslands) {
        for (final Island island : loadedIslands.values()) {
            IslandBoard.add(island);
        }
        this.islands.putAll(loadedIslands);
    }

    public @NotNull IslandCreationResult createIsland(final int slot,
            final @NotNull String category) {
        return createIsland(slot, category, "");
    }

    /**
     * If the island doesn't exist, create it
     *
     * @param slot The island's slot.
     * @param category The category of the island.
     * @param schematic The name of the schematic to load. If it's empty, the island will be
     * empty.
     * @return The IslandCreationResult enum.
     */
    public @NotNull IslandCreationResult createIsland(final int slot,
            final @NotNull String category,
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

    /**
     * Return the island at the given slot.
     *
     * @param slot The slot number of the island.
     * @return The Island object that is stored in the slot that is passed in.
     */
    public @Nullable Island findIslandBy(final int slot) {
        return this.islands.get(slot);
    }

    /**
     * Find an island by its category.
     *
     * @param category The category of the island to find.
     * @return The Island object that matches the category.
     */
    public @Nullable Island findIslandBy(final String category) {
        for (final Island island : this.islands.values()) {
            if (island.getCategory().equals(category)) {
                return island;
            }
        }
        return null;
    }

    /**
     * This function deletes an island from the database and removes it from the island board
     *
     * @param slot The slot of the island you want to delete.
     * @return The Island that was deleted.
     */
    public @Nullable Island deleteIsland(final int slot) {
        final Island island = this.islands.remove(slot);

        // if the island is not null, wipe said island & return deleted island!
        if (island != null) {
            Databases.ISLAND_DATABASE.delete(slot);
            IslandBoard.remove(island);

            SchematicManager.clearPlot(slot);

            return island;
        }
        return null;
    }

    /**
     * Return a collection of all the islands in the map
     *
     * @return The unmodifiable collection of all the islands in the world.
     */
    public @NotNull Collection<Island> getIslands() {
        return Collections.unmodifiableCollection(this.islands.values());
    }

    // This is a Java enum. It's a way to create a set of constants.
    public enum IslandCreationResult {
        UNKNOWN_SCHEMATIC, ISLAND_ALREADY_EXISTS, SUCCESS
    }
}
