package com.github.tofpu.speedbridge2.island.setup;

import com.github.tofpu.speedbridge2.object.Location;
import com.github.tofpu.speedbridge2.object.player.OnlinePlayer;
import com.github.tofpu.speedbridge2.service.Service;

import java.io.File;
import java.util.UUID;

public class IslandSetupController implements Service {
    private final IslandSetupHandler setupHandler;

    public IslandSetupController(IslandSetupHandler setupHandler) {
        this.setupHandler = setupHandler;
    }

    public void begin(final OnlinePlayer player, final int slot, final File schematicFile) {
        setupHandler.start(player, slot, schematicFile);
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
