package io.tofpu.speedbridge2.island.service;

import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.island.domain.IslandRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IslandService {
    private final Map<Integer, Island> islandMap = new HashMap<>();
    private final IslandRepository repository;

    public IslandService(IslandRepository repository) {
        this.repository = repository;
    }

    public void load() {
        Collection<Island> islands = repository.findAll();
        for (Island island : islands) {
            islandMap.put(island.slot(), island);
        }
    }

    public void registerIsland(Island island) {
        islandMap.put(island.slot(), island);
        repository.saveOrUpdate(island);
    }

    public Optional<Island> island(int slot) {
        return Optional.ofNullable(islandMap.get(slot));
    }

    public Island getIsland(int slot) {
        return islandMap.get(slot);
    }

    public void removeIsland(int slot) {
        islandMap.remove(slot);
        repository.deleteBySlot(slot);
    }

    public Collection<Island> islands() {
        return Collections.unmodifiableCollection(islandMap.values());
    }
}
