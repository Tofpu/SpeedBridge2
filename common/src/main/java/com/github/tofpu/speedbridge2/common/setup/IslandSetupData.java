package com.github.tofpu.speedbridge2.common.setup;

import com.github.tofpu.speedbridge2.common.gameextra.land.object.Land;
import com.github.tofpu.speedbridge2.object.Location;
import io.github.tofpu.speedbridge.gameengine.GameData;

public class IslandSetupData extends GameData {
    private final IslandSetupPlayer player;
    private final int slot;
    private final String schematicName;
    private final Land land;

    private Location origin;
    private boolean cancelled = false;

    public IslandSetupData(IslandSetupPlayer player, int slot, String schematicName, Land land) {
        this.player = player;
        this.slot = slot;
        this.schematicName = schematicName;
        this.land = land;
    }

    public IslandSetupPlayer player() {
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

    public void origin(Location origin) {
        // we need the relative coords as the positioning of the land will vary
        Location relativeLocation = land().getIslandLocation().subtract(origin);
        // use the origin's yaw and pitch
        relativeLocation = relativeLocation.setYaw(origin.getYaw()).setPitch(origin.getPitch());

        this.origin = relativeLocation;
    }

    public Location origin() {
        return origin;
    }

    public boolean cancelled() {
        return cancelled;
    }

    public void cancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isReady() {
        return origin() != null;
    }
}
