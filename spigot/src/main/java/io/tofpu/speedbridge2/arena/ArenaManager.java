package io.tofpu.speedbridge2.arena;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.regions.Region;
import io.tofpu.speedbridge2.schematic.Schematic;
import io.tofpu.speedbridge2.util.Position;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;

/**
 * This class is responsible for managing the positioning of arenas in the world.
 */
public class ArenaManager<K> {

    private final World world;
    private final PositionCalculator<K> positionCalculator;

    private final Map<K, Arena> arenas = new HashMap<>();

    public ArenaManager(World world, PositionCalculator<K> positionCalculator) {
        this.world = world;
        this.positionCalculator = positionCalculator;
    }

    private Arena createArena(K key, Schematic schematic) {
        Clipboard clipboard = schematic.clipboard().to();
        Region region = clipboard.getRegion();
        Position position = positionCalculator.reserve(key, region.getWidth());

        return new Arena(world, position, schematic);
    }

    private Arena occupyArena(K key, Schematic schematic) {
        Arena arena = arenas.computeIfAbsent(key, k -> createArena(k, schematic));
        // in case the arena was destroyed directly via the Arena class (Arena#destroy())
        if (arena.has(Arena.GenerationState.DESTROYED)) {
            cleanUpDestroyedArena(key, arena);
            arena = createArena(key, schematic);
        }
        // indicates the arena is now occupied
        arena.set(Arena.OccupationState.OCCUPIED);
        return arena;
    }

    private void cleanUpDestroyedArena(K key, Arena arena) {
        if (!arena.has(Arena.GenerationState.DESTROYED)) {
            throw new IllegalStateException("Arena is not destroyed");
        }
        releaseArena(key);
    }

    /**
     * Generates a new arena with the given key and schematic. If an arena already exists with the given key, this
     * method does nothing.
     *
     * @param key       the key to use for retrieving the arena
     * @param schematic the schematic to use for creating the arena
     * @return the existing arena if one exists, or null if no arena exists
     */
    public Arena generateArena(K key, Schematic schematic) {
        Arena arena = occupyArena(key, schematic);
        // only generate the arena if it has not been generated yet (in case the arena was previously occupied and later
        // freed)
        if (!arena.has(Arena.GenerationState.GENERATED)) {
            arena.generate();
        }
        return arena;
    }

    /**
     * Marks the arena with the given key as free. If the arena does not exist, this method does nothing.
     *
     * @param key the key to use for retrieving the arena
     */
    public void markArenaAsFree(K key) {
        Arena arena = arenas.get(key);
        if (arena == null) {
            return;
        }
        arena.set(Arena.OccupationState.FREE);
    }

    /**
     * Removes and destroys the arena with the given key. If the arena does not exist, this method does nothing.
     *
     * @param key        the key to use for retrieving the arena
     * @param preDestroy a runnable to run before destroying the arena
     */
    public void destroyArena(K key, Runnable preDestroy) {
        Arena arena = arenas.remove(key);
        if (arena == null) {
            return;
        }
        preDestroy.run();
        arena.destroy();

        releaseArena(key);
    }

    /**
     * Releases the arena with the given key. This will allow the position of the arena to be reused by another arena.
     *
     * @param key the key to use for retrieving the arena
     */
    public void releaseArena(K key) {
        arenas.remove(key);
        // allows the position of the arena to be reused by another arena
        positionCalculator.release(key);
    }
}
