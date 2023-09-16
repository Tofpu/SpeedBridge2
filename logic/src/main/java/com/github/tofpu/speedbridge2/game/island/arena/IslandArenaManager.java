package com.github.tofpu.speedbridge2.game.island.arena;

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

    private final World world;
    private final ClipboardPaster clipboardPaster;

    private final AtomicInteger xIncrementer = new AtomicInteger();
    private final Queue<Land> availableLands = new LinkedList<>();

    public IslandArenaManager(World world, ClipboardPaster clipboardPaster) {
        this.world = world;
        this.clipboardPaster = clipboardPaster;
    }

    protected Land getAvailableLand() {
        return availableLands.poll();
    }

    @Override
    public Land generate(final Island island) {
        Land land = getAvailableLand();
        if (land == null) {
            land = generateAndLock(island);
        };
        return land;
    }

    protected Land generateAndLock(final Island island) {
        Island.IslandSchematic schematic = island.getSchematic();
        RegionInfo region = clipboardPaster.getRegion(schematic.getSchematicFile());
        Position landPosition = determine(region);
        clipboardPaster.paste(schematic.getSchematicFile(), landPosition);
        return new Land(landPosition, region, island.getSchematic().getAbsolute());
    }

    @Override
    public void unlock(Land land) {
        availableLands.add(land);
    }

    public Position determine(RegionInfo region) {
        return new Position(world, xIncrementer.getAndAdd(region.getWidth() + ISLAND_GAP), 100, 100);
    }
}
