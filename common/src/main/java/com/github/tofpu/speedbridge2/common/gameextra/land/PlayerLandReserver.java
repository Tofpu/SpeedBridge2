package com.github.tofpu.speedbridge2.common.gameextra.land;

import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.common.island.Island;
import com.github.tofpu.speedbridge2.common.schematic.SchematicHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLandReserver {
    private final SchematicLandReserver landReserver;

    private final Map<UUID, Land> reservedLandsRegistry = new HashMap<>();

    public PlayerLandReserver(SchematicHandler schematicHandler, LandReserver landReserver) {
        this.landReserver = new SchematicLandReserver(schematicHandler, landReserver);
    }

    public Land reserveSpot(UUID playerId, Island island) {
        Land land = landReserver.reserveSpot(island);
        reservedLandsRegistry.put(playerId, land);
        return land;
    }

    public void releaseSpot(UUID playerId, boolean clearSpot) {
        Land land = getReservedSpot(playerId);
        if (land == null) {
            throw new RuntimeException(String.format("There is no reserved spot for %s", playerId));
        }
        landReserver.releaseSpot(land, clearSpot);
    }

    public void releaseSpot(Land land) {
        landReserver.releaseSpot(land, false);
    }

    public void releaseSpot(UUID playerId) {
        releaseSpot(playerId, false);
    }

    public void clearSpot(Land land) {
        landReserver.clear(land);
    }

    public Land getReservedSpot(UUID playerId) {
        return reservedLandsRegistry.get(playerId);
    }

    public boolean hasAvailableLand(int slot) {
        return landReserver.hasAvailableLand(slot);
    }
}
