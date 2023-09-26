package com.github.tofpu.speedbridge2.game;

import com.github.tofpu.speedbridge2.game.core.arena.ClipboardPaster;
import com.github.tofpu.speedbridge2.game.island.Island;
import com.github.tofpu.speedbridge2.game.island.arena.IslandArenaManager;
import com.github.tofpu.speedbridge2.game.island.arena.Land;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArenaManagerTest {
    private final IslandArenaManager arenaManager = new IslandArenaManager(new World(), ClipboardPaster.empty());

    @Test
    void island_reserve_test() {
        Island island = new Island(1, new Island.IslandSchematic(new Location(), null));
        int islandSlot = island.getSlot();

        Land land;

        assertFalse(arenaManager.hasAvailableLand(islandSlot));
        land = arenaManager.generate(island); // generates new land
        assertFalse(arenaManager.hasAvailableLand(islandSlot));

        arenaManager.unlock(land); // puts the land into reserve for future use
        assertTrue(arenaManager.hasAvailableLand(islandSlot));

        arenaManager.generate(island); // uses the reserved land
        assertFalse(arenaManager.hasAvailableLand(islandSlot));
    }

    @Test
    void multiple_island_reserve_test() {
        Island primaryIsland = new Island(1, new Island.IslandSchematic(new Location(), null));
        int primaryIslandSlot = primaryIsland.getSlot();
        Island secondaryIsland = new Island(2, new Island.IslandSchematic(new Location(), null));
        int secondaryIslandSlot = secondaryIsland.getSlot();

        // generates new lands
        Land primaryLand = arenaManager.generate(primaryIsland);
        Land secondaryLand = arenaManager.generate(secondaryIsland);

        // puts the lands into reserve for future use
        arenaManager.unlock(primaryLand);
        assertTrue(arenaManager.hasAvailableLand(primaryIslandSlot));

        arenaManager.unlock(secondaryLand);
        assertTrue(arenaManager.hasAvailableLand(primaryIslandSlot));

        // uses the reserved lands
        arenaManager.generate(secondaryIsland);
        assertFalse(arenaManager.hasAvailableLand(secondaryIslandSlot));

        arenaManager.generate(primaryIsland);
        assertFalse(arenaManager.hasAvailableLand(primaryIslandSlot));
    }
}
