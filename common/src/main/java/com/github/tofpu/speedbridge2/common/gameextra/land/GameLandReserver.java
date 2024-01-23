package com.github.tofpu.speedbridge2.common.gameextra.land;

import com.github.tofpu.speedbridge2.common.gameextra.land.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.LandController;
import com.github.tofpu.speedbridge2.common.gameextra.land.arena.IslandSchematic;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.schematic.Schematic;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import com.github.tofpu.speedbridge2.object.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GameLandReserver {
    private final World world;
    private final SchematicHandler schematicHandler;
    private final LandController landController;

    public GameLandReserver(World world, SchematicHandler schematicHandler, LandController landController) {
        this.world = world;
        this.schematicHandler = schematicHandler;
        this.landController = landController;
    }

    public Land reserveSpot(UUID playerId, Island island) {
        IslandSchematic islandSchematic = createIslandSchematic(island);
        return this.landController.reserveSpot(playerId, islandSchematic, world);
    }

    public void releaseSpot(UUID playerId) {
        landController.releaseSpot(playerId);
    }

    public Land generate(Island island) {
        IslandSchematic islandSchematic = createIslandSchematic(island);
        return landController.arenaManager().generate(islandSchematic, world);
    }

    @NotNull
    private IslandSchematic createIslandSchematic(Island island) {
        Schematic schematic = schematicHandler.resolve(island.getSchematicName());
        return new IslandSchematic(island.getSlot(), schematic, island.getAbsolute());
    }

    public void unlock(Land land) {
        landController.arenaManager().unlock(land);
    }

    public void clear(Land land) {
        landController.arenaManager().clear(land);
    }

    public boolean hasAvailableLand(int slot) {
        return landController.arenaManager().hasAvailableLand(slot);
    }
}
