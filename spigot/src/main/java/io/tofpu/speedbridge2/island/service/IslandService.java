package io.tofpu.speedbridge2.island.service;

import io.tofpu.speedbridge2.group.domain.Group;
import io.tofpu.speedbridge2.group.service.GroupService;
import io.tofpu.speedbridge2.island.domain.Island;
import io.tofpu.speedbridge2.island.domain.IslandDescriptor;
import io.tofpu.speedbridge2.island.domain.IslandRepository;
import io.tofpu.speedbridge2.island.domain.UnresolvedIsland;
import io.tofpu.speedbridge2.island.domain.UnresolvedReason;
import io.tofpu.speedbridge2.island.domain.ValidatableIsland;
import io.tofpu.speedbridge2.schematic.domain.Schematic;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IslandService {
    private final Map<Integer, Island> islandMap = new HashMap<>();
    private final List<UnresolvedIsland> unresolvedIslands = new ArrayList<>();
    private final IslandRepository repository;

    public IslandService(IslandRepository repository) {
        this.repository = repository;
    }

    public void load() {
        Collection<ValidatableIsland> islands = repository.findAll();
        for (ValidatableIsland representation : islands) {
            if (representation.isValid()) {
                islandMap.put(representation.slot(), (Island) representation);
            } else {
                unresolvedIslands.add((UnresolvedIsland) representation);
            }
        }
    }

    public int reResolveAllIslands(GroupService groupService, SchematicHandler schematicHandler) {
        int unresolvedAmount = reResolveValidIslands(groupService, schematicHandler);
        int resolvedAmount = reResolveBrokenIslands(groupService, schematicHandler);
        // total modification count
        return unresolvedAmount + resolvedAmount;
    }

    private int reResolveBrokenIslands(GroupService groupService, SchematicHandler schematicHandler) {
        Iterator<UnresolvedIsland> iterator = unresolvedIslands.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            UnresolvedIsland next = iterator.next();
            boolean groupExists = groupService.contains(next.groupId());
            boolean schematicExists = schematicHandler.contains(next.schematicName());
            if (!groupExists || !schematicExists) {
                continue; // still unresolved
            }
            Group group = groupService.getById(next.groupId());
            if (group == null) continue;
            Schematic schematic = schematicHandler.resolveSchematic(next.schematicName());
            if (schematic == null) continue;
            Island island = new Island(
                    next.slot(),
                    group,
                    schematic,
                    next.position()
            );
            islandMap.put(island.slot(), island); // add it to valid island map
            iterator.remove(); // remove it from unresolved islands collection
            count++;
        }
        return count;
    }

    private int reResolveValidIslands(GroupService groupService, SchematicHandler schematicHandler) {
        Iterator<Island> iterator = islandMap.values().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Island next = iterator.next();
            boolean groupStillExists = groupService.contains(next.groupId());
            boolean schematicStillExists = schematicHandler.contains(next.schematicName());
            // checking if both group and schematic still exist
            if (groupStillExists && schematicStillExists) {
                continue; // still valid
            }
            // otherwise, we need to mark this island as unresolved since we cannot
            // resolve either group or schematic anymore

            Collection<UnresolvedReason> unresolvedReasons = new ArrayList<>();
            if (!groupStillExists) {
                unresolvedReasons.add(UnresolvedReason.MISSING_GROUP);
            }
            if (!schematicStillExists) {
                unresolvedReasons.add(UnresolvedReason.MISSING_SCHEMATIC);
            }
            UnresolvedReason[] reasonsArray = unresolvedReasons.toArray(UnresolvedReason.EMPTY);
            IslandDescriptor impl = IslandDescriptor.from(next);
            UnresolvedIsland unresolvedIsland = new UnresolvedIsland(reasonsArray, impl);
            unresolvedIslands.add(unresolvedIsland); // register it to unresolved island collection
            iterator.remove(); // remove it from valid island collection
            count++;
        }
        return count;
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

    public Collection<ValidatableIsland> islands(boolean validOnly) {
        Collection<ValidatableIsland> islands = new ArrayList<>();
        this.islandMap.forEach((id, island) -> islands.add(island));
        if (validOnly) {
            return Collections.unmodifiableCollection(islands);
        }
        islands.addAll(unresolvedIslands);
        return Collections.unmodifiableCollection(islands);
    }
}
