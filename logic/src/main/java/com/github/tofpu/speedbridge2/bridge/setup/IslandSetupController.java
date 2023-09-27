package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.bridge.setup.BridgeSetupHandler;
import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.Service;

import java.util.UUID;

public class IslandSetupController implements Service {
    private final BridgeSetupHandler setupHandler;

    public IslandSetupController(BridgeSetupHandler setupHandler) {
        this.setupHandler = setupHandler;
    }

    public void begin(final OnlinePlayer player, final int slot, final String schematicName) {
        setupHandler.start(player, slot, schematicName);
    }

    public void cancel(OnlinePlayer player) {
        setupHandler.stop(player.id());
    }

    public boolean isInSetup(UUID playerId) {
        return setupHandler.isInGame(playerId);
    }

    public void setOrigin(UUID playerId, Location location) {
        setupHandler.setOrigin(playerId, location);
    }
}
