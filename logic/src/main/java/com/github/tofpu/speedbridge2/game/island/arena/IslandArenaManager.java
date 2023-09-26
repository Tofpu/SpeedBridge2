package com.github.tofpu.speedbridge2.game.island.arena;

import com.github.tofpu.speedbridge2.ArenaAdapter;
import com.github.tofpu.speedbridge2.game.core.arena.ArenaManager;
import com.github.tofpu.speedbridge2.game.core.arena.ClipboardPaster;
import com.github.tofpu.speedbridge2.game.island.Island;
import com.github.tofpu.speedbridge2.object.Position;
import com.github.tofpu.speedbridge2.object.World;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class IslandArenaManager implements ArenaManager {
    // todo: make this option configurable
    private static final int ISLAND_GAP = 10;

    private final ArenaAdapter arenaAdapter;
    private final World world;
    private final ClipboardPaster clipboardPaster;

    private final AtomicInteger xIncrementer = new AtomicInteger();
    private final Queue<Land> landReserves = new LinkedList<>();

    public IslandArenaManager(ArenaAdapter arenaAdapter) {
        this.arenaAdapter = arenaAdapter;
        this.world = arenaAdapter.gameWorld();
        this.clipboardPaster = arenaAdapter.clipboardPaster();
    }

    @Override
    public void prepare() {
        arenaAdapter.resetGameWorld();
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
    public Land generate(final Island island) {
        Land land = getAvailableLand(island.getSlot());
        if (land == null) {
            land = generateAndLock(island);
        }
        return land;
    }

    protected Land generateAndLock(final Island island) {
        Island.IslandSchematic schematic = island.getSchematic();
        RegionInfo region = clipboardPaster.getRegion(schematic.getSchematicFile());
        Position landPosition = determine(region);
        clipboardPaster.paste(schematic.getSchematicFile(), landPosition);
        return new Land(island.getSlot(), landPosition, region, island.getSchematic().getAbsolute());
    }

    @Override
    public void unlock(Land land) {
        landReserves.add(land);
    }

    protected Position determine(RegionInfo region) {
        return new Position(world, xIncrementer.getAndAdd(region.getWidth() + ISLAND_GAP), 100, 100);
    }
}
