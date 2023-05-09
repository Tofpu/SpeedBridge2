package io.tofpu.speedbridge2.game.arena;

import io.tofpu.speedbridge2.game.IslandGameSession;
import io.tofpu.speedbridge2.game.arena.land.LandArea;
import io.tofpu.speedbridge2.game.arena.land.LandSchematic;
import io.tofpu.speedbridge2.game.bukkit.BukkitLandArea;
import io.tofpu.speedbridge2.game.generic.Position;
import io.tofpu.speedbridge2.game.generic.World;
import io.tofpu.speedbridge2.game.exception.NonExistantWorldException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tofpu.gameengine.util.ProgramCorrectnessHelper.requireState;

public class StandardArenaManager implements GameArenaManager {
    private final AtomicInteger counter = new AtomicInteger(100);
    private final Map<Integer, Collection<LandArea>> landAreaMap = new HashMap<>();
    private final Map<UUID, LandArea> reservedMap = new HashMap<>();
    private final World world;
    private final int islandGap;

    public StandardArenaManager(final World world, int islandGap) throws NonExistantWorldException {
        this.islandGap = islandGap;
        if (world == null) {
            throw new NonExistantWorldException();
        }
        this.world = world;
    }

    @Override
    public LandArea reserveArea(final IslandGameSession session, final LandSchematic schematic) {
        LandArea landArea = findAvailableLand(session.getSlot(), constructNewLand(session.getSlot(), schematic));
        landArea.reserved(true);

        this.reservedMap.put(session.getId(), landArea);

        return landArea;
    }

    @Override
    public void unreserveArea(final UUID id) {
        requireState(isReserved(id), "There is no reservation for a game with id %s", id);

        LandArea landArea = this.reservedMap.get(id);
        landArea.reserved(false);
    }

    private LandArea constructNewLand(final int slot, final LandSchematic schematic) {
//        final double[] positions = {counter.get(), 100, 100};

        LandArea landArea = new BukkitLandArea(world, Position.of(counter.get(), 100, 100));
        counter.getAndAdd(schematic.getWidth() + islandGap);

        registerLandArea(slot, landArea);
        landArea.generateLand(schematic);

        return landArea;
    }

    private void registerLandArea(int slot, LandArea newLandArea) {
        this.landAreaMap.compute(slot, (integer, landAreas) -> {
            if (landAreas == null) {
                landAreas = new HashSet<>();
            }
            landAreas.add(newLandArea);
            return landAreas;
        });
    }

    @Override
    public boolean isReserved(UUID id) {
        return this.reservedMap.containsKey(id);
    }

    private LandArea findAvailableLand(int slot, LandArea defaultLand) {
        return this.landAreaMap.get(slot).stream()
                .filter(landArea -> !landArea.isReserved())
                .findFirst().orElse(defaultLand);
    }
}
