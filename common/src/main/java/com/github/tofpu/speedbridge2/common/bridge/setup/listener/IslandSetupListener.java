package com.github.tofpu.speedbridge2.common.bridge.setup.listener;

import com.github.tofpu.speedbridge2.common.bridge.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.common.bridge.setup.SetupPlayer;
import com.github.tofpu.speedbridge2.common.bridge.setup.event.StopIslandSetupEvent;
import com.github.tofpu.speedbridge2.common.gameextra.land.LandController;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.event.dispatcher.ListeningState;

public class IslandSetupListener implements Listener {

    private final IslandService islandService;
    private final LobbyService lobbyService;
    private final LandController landController;

    public IslandSetupListener(IslandService islandService, LobbyService lobbyService, LandController landController) {
        this.islandService = islandService;
        this.lobbyService = lobbyService;
        this.landController = landController;
    }

    @EventListener(state = ListeningState.MONITORING, ignoreCancelled = true)
    public void on(final StopIslandSetupEvent event) {
        IslandSetupData data = event.islandSetup().data();
        if (data.origin() == null) return;
        try {
            islandService.register(data.slot(), data.origin(), data.schematicName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SetupPlayer setupPlayer = data.player();
        landController.releaseSpot(setupPlayer.id());

        setupPlayer.player().teleport(lobbyService.position());
    }
}
