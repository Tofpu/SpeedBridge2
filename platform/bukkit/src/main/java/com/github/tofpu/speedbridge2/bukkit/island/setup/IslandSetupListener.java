package com.github.tofpu.speedbridge2.bukkit.island.setup;

import com.github.tofpu.speedbridge2.bukkit.helper.CoreConversionHelper;
import com.github.tofpu.speedbridge2.common.setup.GameSetupSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.UUID;

public class IslandSetupListener implements Listener {
    private final GameSetupSystem setupSystem;

    public IslandSetupListener(GameSetupSystem setupSystem) {
        this.setupSystem = setupSystem;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void on(final PlayerToggleSneakEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!setupSystem.isInSetup(playerId)) {
            return;
        }

        setupSystem.setOrigin(playerId, CoreConversionHelper.toLocation(event.getPlayer().getLocation()));
    }
}
