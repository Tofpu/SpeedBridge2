package com.github.tofpu.speedbridge2.common.island;

import com.github.tofpu.speedbridge2.database.service.DatabaseService;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.service.LoadableService;

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
        databaseService.executeSync(session -> {
            List<Island> islands = session.createQuery("FROM Island").list();
            islands.forEach(this::add);
        });
    }

    @Override
    public void unload() {
        this.islandMap.clear();
    }

    public void register(int slot, Location origin, String schematicName) {
        System.out.println("Creating island with origin: " + origin);
        Island island = new Island(slot, origin, schematicName);

        databaseService.executeSync(session -> {
            Island data = session.find(Island.class, slot);
            if (data == null) {
                session.persist(island);
            } else {
                session.merge(island);
            }
        });
        add(island);
    }

    private void add(final Island island) {
        System.out.println("Adding island: " + island);
        this.islandMap.put(island.getSlot(), island);
    }

    public Island get(int slot) {
        return this.islandMap.get(slot);
    }
}
