package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.game.land.arena.IslandSchematic;
import com.github.tofpu.speedbridge2.common.game.land.Land;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandArenaManager;
import com.github.tofpu.speedbridge2.object.World;
import com.github.tofpu.speedbridge2.common.schematic.Schematic;
import com.github.tofpu.speedbridge2.common.schematic.SchematicResolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArenaManagerTest {
    private final SchematicResolver schematicResolver = SchematicResolver.empty();
    private final IslandArenaManager arenaManager = new IslandArenaManager(PlatformArenaAdapter.simple(new World(), ClipboardPaster.empty(), schematicResolver));

    @Test
    void island_reserve_test() {
        World world = new World();
        int islandSlot = 1;

        IslandSchematic islandSchematic = new IslandSchematic(islandSlot, new Schematic(null, schematicResolver.origin(null)), world);

        Land land;

        assertFalse(arenaManager.hasAvailableLand(islandSlot));
        land = arenaManager.generate(islandSchematic, world); // generates new land
        assertFalse(arenaManager.hasAvailableLand(islandSlot));

        arenaManager.unlock(land); // puts the land into reserve for future use
        assertTrue(arenaManager.hasAvailableLand(islandSlot));

        arenaManager.generate(islandSchematic, world); // uses the reserved land
        assertFalse(arenaManager.hasAvailableLand(islandSlot));
    }

    @Test
    void multiple_island_reserve_test() {
        World world = new World();
        IslandSchematic primaryIsland = new IslandSchematic(1, new Schematic(null, schematicResolver.origin(null)), world);
        int primaryIslandSlot = primaryIsland.slot();
        IslandSchematic secondaryIsland = new IslandSchematic(2, new Schematic(null, schematicResolver.origin(null)), world);
        int secondaryIslandSlot = secondaryIsland.slot();

        // generates new lands
        Land primaryLand = arenaManager.generate(primaryIsland, new World());
        Land secondaryLand = arenaManager.generate(secondaryIsland, world);

        // puts the lands into reserve for future use
        arenaManager.unlock(primaryLand);
        assertTrue(arenaManager.hasAvailableLand(primaryIslandSlot));

        arenaManager.unlock(secondaryLand);
        assertTrue(arenaManager.hasAvailableLand(primaryIslandSlot));

        // uses the reserved lands
        arenaManager.generate(secondaryIsland, world);
        assertFalse(arenaManager.hasAvailableLand(secondaryIslandSlot));

        arenaManager.generate(primaryIsland, world);
        assertFalse(arenaManager.hasAvailableLand(primaryIslandSlot));
    }
}
