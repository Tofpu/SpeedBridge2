package com.github.tofpu.speedbridge2.island.setup;

import com.github.tofpu.speedbridge2.game.core.Game;
import com.github.tofpu.speedbridge2.game.core.GamePlayer;
import com.github.tofpu.speedbridge2.game.island.arena.Land;
import com.github.tofpu.speedbridge2.object.Location;

import java.io.File;

public class IslandSetup extends Game {
    private final int slot;
    private final File schematicFile;
    private final Land land;
    private Location origin;
    public IslandSetup(GamePlayer gamePlayer, int slot, File schematicFile, Land land) {
        super(gamePlayer);
        this.slot = slot;
        this.schematicFile = schematicFile;
        this.land = land;
    }

    public int slot() {
        return slot;
    }

    public Land land() {
        return land;
    }

    public File schematicFile() {
        return schematicFile;
    }

    public void origin(Location origin) {
        this.origin = origin;
    }

    public Location origin() {
        return origin;
    }
}
