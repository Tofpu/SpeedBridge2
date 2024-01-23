package com.github.tofpu.speedbridge2.common.setup.game;

import com.github.tofpu.speedbridge2.common.gameextra.land.Land;
import com.github.tofpu.speedbridge2.object.Location;
import io.github.tofpu.speedbridge.gameengine.GameData;

public class IslandSetupData extends GameData {
    private final SetupPlayer player;
    private final int slot;
    private final String schematicName;
    private final Land land;

    private Location originalLocation;
    private Location origin;

    public IslandSetupData(SetupPlayer player, int slot, String schematicName, Land land) {
        this.player = player;
        this.slot = slot;
        this.schematicName = schematicName;
        this.land = land;
    }

    public SetupPlayer player() {
        return player;
    }

    public int slot() {
        return slot;
    }

    public Land land() {
        return land;
    }

    public String schematicName() {
        return schematicName;
    }

    public void originalLocation(Location originalLocation) {
        this.originalLocation = originalLocation;
    }

    public Location originalLocation() {
        return originalLocation;
    }

    public void origin(Location origin) {
        this.origin = origin;
    }

    public Location origin() {
        return origin;
    }
}
