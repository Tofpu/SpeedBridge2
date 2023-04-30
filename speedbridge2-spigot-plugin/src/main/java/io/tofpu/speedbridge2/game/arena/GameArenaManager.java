package io.tofpu.speedbridge2.game.arena;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import io.tofpu.speedbridge2.game.exception.NonExistantWorldException;
import io.tofpu.speedbridge2.model.common.config.manager.ConfigurationManager;
import org.bukkit.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GameArenaManager {
    private final AtomicInteger counter = new AtomicInteger(100);
    private final Map<Integer, Collection<LandArea>> landAreaMap = new HashMap<>();
    private final World world;

    public GameArenaManager(final World world) throws NonExistantWorldException {
        if (world == null) {
            throw new NonExistantWorldException();
        }
        this.world = world;
    }

    public LandArea reserveArea(final int slot, final Clipboard clipboard) {
        LandArea availableReserves = findAvailableLand(slot, constructNewLand(slot, clipboard));
        availableReserves.setReserved(true);

        return availableReserves;
    }

    private LandArea constructNewLand(final int slot, final Clipboard clipboard) {
        final double[] positions = {counter.get(), 100, 100};

        LandArea newLandArea = new LandArea(world, slot, clipboard, positions);
        counter.getAndAdd(newLandArea.getWidth() + ConfigurationManager.INSTANCE.getGeneralCategory().getIslandSpaceGap());

        registerLandArea(slot, newLandArea);
        generateLandArea(newLandArea);

        return newLandArea;
    }

    private void generateLandArea(LandArea newLandArea) {
        newLandArea.generate(world);
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

    private LandArea findAvailableLand(int slot, LandArea defaultLand) {
        return this.landAreaMap.get(slot).stream()
                .filter(landArea -> landArea.getSlot() == slot)
                .filter(landArea -> !landArea.isReserved())
                .findFirst().orElse(defaultLand);
    }
}
