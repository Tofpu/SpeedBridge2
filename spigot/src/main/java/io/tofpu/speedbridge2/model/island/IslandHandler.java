package io.tofpu.speedbridge2.model.island;

import io.tofpu.speedbridge2.model.common.database.Databases;
import io.tofpu.speedbridge2.model.common.util.BridgeUtil;
import io.tofpu.speedbridge2.model.island.arena.ArenaManager;
import io.tofpu.speedbridge2.model.island.object.Island;
import io.tofpu.speedbridge2.model.island.object.IslandBuild;
import io.tofpu.speedbridge2.model.leaderboard.IslandBoard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class IslandHandler {
    private final @NotNull Map<Integer, Island> islandMap = new HashMap<>();
    private ArenaManager arenaManager;

    public void init(final @NotNull ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    /**
     * Loads the islands from the map of islands
     *
     * @param loadedIslands A map of island ids to island objects.
     */
    public void load(final @NotNull Map<Integer, Island> loadedIslands) {
        for (final Island island : loadedIslands.values()) {
            IslandBoard.add(island);
        }
        this.islandMap.putAll(loadedIslands);
    }

    /**
     * Creates an island
     *
     * @param slot      The island's slot.
     * @param category  The category of the island.
     * @param schematic The name of the schematic to load. It cannot be null nor empty.
     * @return The {@link IslandHandler.IslandCreationResult} enum.
     */
    public @NotNull IslandHandler.IslandCreationResult createIsland(final int slot,
                                                                    final @NotNull String category, final @NotNull String schematic) {
        // if the island does exist, return ALREADY_EXISTS!
        if (islandMap.containsKey(slot)) {
            return IslandCreationResultType.ISLAND_ALREADY_EXISTS.empty();
        }

        final Island island =
                IslandFactory.create(IslandFactory.IslandFactoryType.BUILD, slot,
                        category);

        BridgeUtil.debug("IslandHandler#createIsland(): before: " + island);

        // if the schematic is empty, or it doesn't exist, return UNKNOWN_SCHEMATIC!
        if (schematic.isEmpty() || !island.selectSchematic(schematic)) {
            return IslandCreationResultType.UNKNOWN_SCHEMATIC.empty();
        }

        BridgeUtil.debug("IslandHandler#createIsland(): after: " + island);

        // otherwise, return ISLAND_SUCCESS
        return IslandCreationResultType.SUCCESS.create(island);
    }

    /**
     * Return the island at the given slot.
     *
     * @param slot The slot number of the island.
     * @return The Island object that is stored in the slot that is passed in.
     */
    public @Nullable Island findIslandBy(final int slot) {
        return this.islandMap.get(slot);
    }

    /**
     * Find an island by its category.
     *
     * @param category The category of the island to find.
     * @return The Island object that matches the category.
     */
    public @Nullable Island findIslandBy(final String category) {
        for (final Island island : this.islandMap.values()) {
            if (island.getCategory()
                    .equals(category)) {
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
        final Island island = this.islandMap.remove(slot);

        // if the island is not null, wipe said island & return deleted island!
        if (island != null) {
            Databases.ISLAND_DATABASE.delete(slot);
            IslandBoard.remove(island);

            arenaManager.clearPlot(slot);

            return island;
        }
        return null;
    }

    /**
     * Return a collection of all the islands in the map
     *
     * @return The unmodifiable collection of all the islands in the world.
     */
    public @NotNull Collection<Island> getIslandMap() {
        return Collections.unmodifiableCollection(this.islandMap.values());
    }

    /**
     * Return a collection of all the islands integers in the map
     *
     * @return The unmodifiable collection of all the islands integers in the world.
     */
    public @NotNull Collection<Integer> getIntegerIslands() {
        return Collections.unmodifiableCollection(this.islandMap.keySet());
    }

    /**
     * @param island The island to register.
     * @return The registration result.
     */
    public IslandRegistrationResultType registerIsland(Island island) {
        if (islandMap.containsKey(island.getSlot())) {
            return IslandRegistrationResultType.ISLAND_ALREADY_EXISTS;
        }

        if (island.getSchematicClipboard() == null) {
            return IslandRegistrationResultType.UNKNOWN_SCHEMATIC;
        }

        // print the island with BridgeUtil.debug
        BridgeUtil.debug("IslandHandler#registerIsland(): island: " + island);

        if (island instanceof IslandBuild) {
            final IslandBuild islandBuild = (IslandBuild) island;
            island = islandBuild.toRegularIsland();
        }

        islandMap.put(island.getSlot(), island);
        Databases.ISLAND_DATABASE.insert(island);
        IslandBoard.add(island);

        return IslandRegistrationResultType.SUCCESS;
    }

    public enum IslandCreationResultType {
        /**
         * The island had an unknown schematic.
         */
        UNKNOWN_SCHEMATIC,
        /**
         * The island was already registered.
         */
        ISLAND_ALREADY_EXISTS,
        /**
         * The island was successfully registered.
         */
        SUCCESS;

        public IslandCreationResult empty() {
            return create(null);
        }

        public IslandCreationResult create(final Island
                                                   island) {
            return new IslandCreationResult(this, island);
        }
    }

    public enum IslandRegistrationResultType {
        /**
         * The island had an unknown schematic.
         */
        UNKNOWN_SCHEMATIC,
        /**
         * The island was already registered.
         */
        ISLAND_ALREADY_EXISTS,
        /**
         * The island was successfully registered.
         */
        SUCCESS;
    }

    public static class IslandCreationResult {
        private final IslandCreationResultType result;
        private final Island island;

        public IslandCreationResult(final IslandCreationResultType result, final Island island) {
            this.result = result;
            this.island = island;
        }

        public IslandCreationResultType getResult() {
            return result;
        }

        public Island getIsland() {
            return island;
        }
    }
}
