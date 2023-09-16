package com.github.tofpu.speedbridge2.island;

import com.github.tofpu.speedbridge2.database.Database;
import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.game.island.Island;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.service.LoadableService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IslandService implements LoadableService {
    private final DatabaseService databaseService;
    private final Map<Integer, Island> islandMap = new HashMap<>();

    public IslandService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void load() {
        databaseService.execute(session -> {
            List<Island> islands = session.createQuery("FROM Island").list();
            islands.forEach(island -> islandMap.put(island.getSlot(), island));
        });
    }

    @Override
    public void unload() {
        this.islandMap.clear();
    }

    public void register(int slot, Location origin, File schematicFile) {
        System.out.println("Creating island with origin: " + origin);
        Island island = new Island(slot, new Island.IslandSchematic(origin, schematicFile));

        databaseService.execute(session -> session.persist(island));
        this.islandMap.put(slot, island);
    }

    public Island get(int slot) {
        return this.islandMap.get(slot);
    }
}
