package com.github.tofpu.speedbridge2.common.game.land.arena;

import com.github.tofpu.speedbridge2.common.ArenaAdapter;
import com.github.tofpu.speedbridge2.common.game.ArenaManager;
import com.github.tofpu.speedbridge2.common.game.ClipboardPaster;
import com.github.tofpu.speedbridge2.common.game.land.Land;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.Vector;
import com.github.tofpu.speedbridge2.object.World;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BasicArenaManager implements ArenaManager {
    protected final ArenaAdapter arenaAdapter;
    protected final ClipboardPaster clipboardPaster;
    protected final Vector initialPosition;

    private final AtomicInteger offsetX;
    private final Queue<Land> landReserves;

    public BasicArenaManager(ArenaAdapter arenaAdapter, Vector initialPosition) {
        this.arenaAdapter = arenaAdapter;
        this.clipboardPaster = arenaAdapter.clipboardPaster();
        this.initialPosition = initialPosition;
        this.offsetX = new AtomicInteger((int) initialPosition.x());
        this.landReserves = new LinkedList<>();
    }

    public boolean hasAvailableLand(int slot) {
        for (Land availableLand : landReserves) {
            if (availableLand.islandSlot() == slot) {
                return true;
            }
        }
        return false;
    }

    public Land viewAvailableLand(int slot) {
        for (Land availableLand : landReserves) {
            if (availableLand.islandSlot() == slot) {
                return availableLand;
            }
        }
        return null;
    }

    protected Land getAvailableLand(int slot) {
        Land land = viewAvailableLand(slot);
        if (land != null) {
            landReserves.remove(land);
        }
        return land;
    }

    @Override
    public Land generate(final IslandSchematic islandSchematic, World world) {
        Land land = getAvailableLand(islandSchematic.slot());
        if (land == null) {
            land = generateAndLock(islandSchematic, world);
        } else {
            land = Land.newLand(land, islandSchematic.absolutePosition());
        }
        return land;
    }

    protected Land generateAndLock(final IslandSchematic islandSchematic, World world) {
        File schematicFile = islandSchematic.schematicFile();
        RegionInfo region = clipboardPaster.getRegion(schematicFile);
        Position landPosition = reservePosition(region, world);

        Land land = Land.newLand(islandSchematic.slot(), landPosition, region, islandSchematic.absolutePosition());
        Location islandLocation = land.getIslandLocation();
        clipboardPaster.paste(schematicFile, new Position(islandLocation.getWorld(), islandLocation.getX(), islandLocation.getY(), islandLocation.getZ()));
        return land;
    }

    @Override
    public void unlock(Land land) {
        landReserves.add(land);
    }

    @Override
    public void clear(Land land) {
        clipboardPaster.clear(land.getMinPoint(), land.getMaxPoint(), land.getLandPosition().getWorld());
        // todo: add a way to indicate that a land must be regenerated
    }

    protected Position reservePosition(RegionInfo region, World world) {
        return new Position(world, offsetX.getAndAdd(region.getWidth() + gapBetweenLand()), (int) initialPosition.y(), (int) initialPosition.z());
    }

    protected int gapBetweenLand() {
        return 10;
    }
}
