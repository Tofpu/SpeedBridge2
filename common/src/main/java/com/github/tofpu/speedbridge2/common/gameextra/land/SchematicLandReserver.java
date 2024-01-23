package com.github.tofpu.speedbridge2.common.gameextra.land;

import com.github.tofpu.speedbridge2.common.gameextra.land.object.IslandSchematic;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.schematic.Schematic;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;
import org.jetbrains.annotations.NotNull;

public class SchematicLandReserver {
    private final SchematicHandler schematicHandler;
    private final LandReserver landReserver;

    public SchematicLandReserver(SchematicHandler schematicHandler, LandReserver landReserver) {
        this.schematicHandler = schematicHandler;
        this.landReserver = landReserver;
    }

    public Land reserveSpot(Island island) {
        IslandSchematic islandSchematic = createIslandSchematic(island);
        return this.landReserver.reserveLand(islandSchematic);
    }

    public void releaseSpot(Land land) {
        releaseSpot(land, false);
    }

    public void releaseSpot(Land land, boolean clearSpot) {
        if (clearSpot) {
            landReserver.clearLand(land);
        } else {
            landReserver.releaseLand(land);
        }
    }

    @NotNull
    private IslandSchematic createIslandSchematic(Island island) {
        Schematic schematic = schematicHandler.resolve(island.getSchematicName());
        return new IslandSchematic(island.getSlot(), schematic, island.getAbsolute());
    }

    public void clear(Land land) {
        landReserver.clearLand(land);
    }

    public boolean hasAvailableLand(int slot) {
        return landReserver.hasAvailableLand(slot);
    }
}
