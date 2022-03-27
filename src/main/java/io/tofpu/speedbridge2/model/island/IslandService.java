package io.tofpu.speedbridge2.model.island;

import io.tofpu.speedbridge2.model.island.object.Island;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class IslandService {
    public static final @NotNull IslandService INSTANCE = new IslandService();

    private final @NotNull IslandHandler islandHandler;
    private final @NotNull IslandRepository islandRepository;

    public IslandService() {
        this.islandHandler = new IslandHandler();
        this.islandRepository = new IslandRepository();
    }

    /**
     * Loads the islands from the map of islands
     */
    public CompletableFuture<Map<Integer, Island>> load() {
        return this.islandRepository.loadIslands().whenComplete((islandMap, throwable) -> {
            this.islandHandler.load(islandMap);
        });
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
    public @NotNull IslandHandler.IslandCreationResult createIsland(final int slot,
            final @NotNull String category,
            final String schematic) {
        return this.islandHandler.createIsland(slot, category, schematic);
    }

    /**
     * Return the island at the given slot.
     *
     * @param slot The slot number of the island.
     * @return The Island object that is stored in the slot that is passed in.
     */
    public @Nullable Island findIslandBy(final int slot) {
        return this.islandHandler.findIslandBy(slot);
    }

    /**
     * Find an island by its category.
     *
     * @param category The category of the island to find.
     * @return The Island object that matches the category.
     */
    public @Nullable Island findIslandBy(final @NotNull String category) {
        return this.islandHandler.findIslandBy(category);
    }

    /**
     * This function deletes an island from the database and removes it from the island board
     *
     * @param slot The slot of the island you want to delete.
     * @return The Island that was deleted.
     */
    public @Nullable Island deleteIsland(final int slot) {
        return this.islandHandler.deleteIsland(slot);
    }

    /**
     * Return a collection of all the islands in the map
     *
     * @return The unmodifiable collection of all the islands in the world.
     */
    public @NotNull Collection<Island> getAllIslands() {
        return this.islandHandler.getIslands();
    }

    /**
     * Return a collection of all the islands integers in the map
     *
     * @return The unmodifiable collection of all the islands integers in the world.
     */
    public @NotNull Collection<Integer> getIntegerIslands() {
        return this.islandHandler.getIntegerIslands();
    }
}
