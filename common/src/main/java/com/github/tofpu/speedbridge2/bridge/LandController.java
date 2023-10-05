package com.github.tofpu.speedbridge2.bridge;

import com.github.tofpu.speedbridge2.bridge.core.arena.ArenaManager;
import com.github.tofpu.speedbridge2.object.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LandController {
    private final ArenaManager arenaManager;

    private final Map<UUID, Land> reservedLandsRegistry = new HashMap<>();

    public LandController(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public Land reserveSpot(UUID playerId, IslandSchematic islandSchematic, World world) {
        Land land = arenaManager.generate(islandSchematic, world);
        reservedLandsRegistry.put(playerId, land);
        return land;
    }

    public void releaseSpot(UUID playerId, boolean clearSpot) {
        Land land = reservedSpot(playerId);
        if (land == null) {
            throw new RuntimeException(String.format("There is no reserved spot for %s", playerId));
        }

        if (clearSpot) {
            arenaManager.clear(land);
        } else {
            arenaManager.unlock(land);
        }
    }

    public void releaseSpot(UUID playerId) {
        releaseSpot(playerId, false);
    }

    public Land reservedSpot(UUID playerId) {
        return reservedLandsRegistry.get(playerId);
    }

    public ArenaManager arenaManager() {
        return arenaManager;
    }
}
