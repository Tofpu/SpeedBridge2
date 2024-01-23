package com.github.tofpu.speedbridge2.common.game;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.gameextra.ClipboardPaster;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.ArenaManagerOptions;
import com.github.tofpu.speedbridge2.common.gameextra.land.BasicLandReserver;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.IslandSchematic;
import com.github.tofpu.speedbridge2.common.schematic.Schematic;
import com.github.tofpu.speedbridge2.common.schematic.SchematicResolver;
import com.github.tofpu.speedbridge2.object.Vector;
import com.github.tofpu.speedbridge2.object.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LandReserverTest {
    private final SchematicResolver schematicResolver = SchematicResolver.empty();
    private final BasicLandReserver arenaManager = new BasicLandReserver(PlatformArenaAdapter.simple(new World(), ClipboardPaster.empty(), schematicResolver), new ArenaManagerOptions(new Vector(0, 0, 0)));

    @Test
    void island_reserve_test() {
        World world = new World();
        int islandSlot = 1;

        IslandSchematic islandSchematic = new IslandSchematic(islandSlot, new Schematic(null, schematicResolver.origin(null)), world);

        Land land;

        assertFalse(arenaManager.hasAvailableLand(islandSlot));
        land = arenaManager.reserveLand(islandSchematic); // generates new land
        assertFalse(arenaManager.hasAvailableLand(islandSlot));

        arenaManager.releaseLand(land); // puts the land into reserve for future use
        assertTrue(arenaManager.hasAvailableLand(islandSlot));

        arenaManager.reserveLand(islandSchematic); // uses the reserved land
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
        Land primaryLand = arenaManager.reserveLand(primaryIsland);
        Land secondaryLand = arenaManager.reserveLand(secondaryIsland);

        // puts the lands into reserve for future use
        arenaManager.releaseLand(primaryLand);
        assertTrue(arenaManager.hasAvailableLand(primaryIslandSlot));

        arenaManager.releaseLand(secondaryLand);
        assertTrue(arenaManager.hasAvailableLand(primaryIslandSlot));

        // uses the reserved lands
        arenaManager.reserveLand(secondaryIsland);
        assertFalse(arenaManager.hasAvailableLand(secondaryIslandSlot));

        arenaManager.reserveLand(primaryIsland);
        assertFalse(arenaManager.hasAvailableLand(primaryIslandSlot));
    }
}
