package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.GamePlayer;
import com.github.tofpu.speedbridge2.bridge.Land;
import com.github.tofpu.speedbridge2.object.Location;

public class IslandSetup extends Game<BridgeSetupHandler, IslandSetup> {
    private final int slot;
    private final String schematicName;
    private final Land land;
    private Location origin;
    public IslandSetup(BridgeSetupHandler handler, GamePlayer gamePlayer, int slot, String schematicName, Land land) {
        super(handler, gamePlayer);
        this.slot = slot;
        this.schematicName = schematicName;
        this.land = land;
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
        this.origin = origin;
    }

    public Location origin() {
        return origin;
    }
}
