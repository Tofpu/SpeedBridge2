package com.github.tofpu.speedbridge2.common.gameextra.land;

import com.github.tofpu.speedbridge2.common.PlatformArenaAdapter;
import com.github.tofpu.speedbridge2.common.gameextra.ClipboardPaster;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.ArenaManagerOptions;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.IslandSchematic;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.common.gameextra.land.object.RegionInfo;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.Vector;
import com.github.tofpu.speedbridge2.object.World;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicLandReserver implements LandReserver {
    protected final ArenaManagerOptions options;
    private final World world;

    protected final ClipboardPaster clipboardPaster;
    private final AtomicInteger offsetX;

    private final Map<Integer, List<Land>> reservedLands;
    private final Queue<Land> releasedLands;

    public BasicLandReserver(PlatformArenaAdapter arenaAdapter, ArenaManagerOptions options) {
        this.options = options;
        this.world = arenaAdapter.gameWorld();

        this.clipboardPaster = arenaAdapter.clipboardPaster();
        this.offsetX = new AtomicInteger((int) initialPosition().x());

        this.reservedLands = new HashMap<>();
        this.releasedLands = new LinkedList<>();
    }

    @Override
    public Land reserveLand(final IslandSchematic islandSchematic) {
        Land land = pollAvailableLand(islandSchematic.slot());
        if (land != null) {
            // sometimes an admin changes the position of an island schematic
            return Land.newLand(land, islandSchematic.absolutePosition());
        }
        return generateAndRegisterLand(islandSchematic);
    }

    protected Land pollAvailableLand(int slot) {
        Land land = peekAtAvailableLand(slot);
        if (land != null) {
            releasedLands.remove(land);
        }
        return land;
    }

    public Land peekAtAvailableLand(int slot) {
        for (Land availableLand : releasedLands) {
            if (availableLand.islandSlot() == slot) {
                return availableLand;
            }
        }
        return null;
    }

    protected Land generateAndRegisterLand(final IslandSchematic islandSchematic) {
        Land land = generateLand(islandSchematic);
        registerLandToReserves(islandSchematic, land);
        return land;
    }

    private Land generateLand(IslandSchematic islandSchematic) {
        File schematicFile = islandSchematic.schematicFile();
        RegionInfo region = clipboardPaster.getRegion(schematicFile);

        Position landPosition = reservePosition(region, world);
        Land land = Land.newLand(islandSchematic.slot(), landPosition, region, islandSchematic.absolutePosition());

        Location islandLocation = land.getIslandLocation();
        clipboardPaster.paste(schematicFile, new Position(world, islandLocation.getX(), islandLocation.getY(), islandLocation.getZ()));

        return land;
    }

    protected Position reservePosition(RegionInfo region, World world) {
        return new Position(world, offsetX.getAndAdd(region.getWidth() + gapBetweenLand()), (int) initialPosition().y(), (int) initialPosition().z());
    }

    private void registerLandToReserves(IslandSchematic islandSchematic, Land land) {
        reservedLands.computeIfAbsent(islandSchematic.slot(), key -> new ArrayList<>()).add(land);
    }

    @Override
    public boolean hasAvailableLand(int slot) {
        for (Land availableLand : releasedLands) {
            if (availableLand.islandSlot() == slot) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void releaseLand(Land land) {
        removeLandFromReserves(land);
        releasedLands.add(land);
    }

    private void removeLandFromReserves(Land land) {
        reservedLands.computeIfPresent(land.getIslandSlot(), (key, lands) -> {
            lands.remove(land);
            return lands;
        });
    }

    @Override
    public void clearLand(Land land, boolean shouldReleaseLand) {
        clipboardPaster.clear(land.getMinPoint(), land.getMaxPoint(), land.getLandPosition().getWorld());
        if (shouldReleaseLand) {
            removeLandFromReserves(land);
            releasedLands.add(land);
        }
        // todo: since we cleared the land, we need to add a way to indicate that a land must be regenerated
    }

    protected int gapBetweenLand() {
        return options.gapBetweenIsland();
    }

    public Vector initialPosition() {
        return options.initialPosition();
    }
}
