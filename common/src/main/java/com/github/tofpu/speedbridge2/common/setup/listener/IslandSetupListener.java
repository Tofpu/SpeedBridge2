package com.github.tofpu.speedbridge2.common.setup.listener;

import com.github.tofpu.speedbridge2.common.PlatformSetupAdapter;
import com.github.tofpu.speedbridge2.common.gameextra.land.PlayerLandReserver;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.setup.GameSetupSystem;
import com.github.tofpu.speedbridge2.common.setup.IslandSetup;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.common.setup.IslandSetupPlayer;
import com.github.tofpu.speedbridge2.common.setup.event.StartIslandSetupEvent;
import com.github.tofpu.speedbridge2.common.setup.event.StopIslandSetupEvent;
import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.event.dispatcher.ListeningState;

public class IslandSetupListener implements Listener {

    private final GameSetupSystem setupSystem;
    private final PlatformSetupAdapter setupAdapter;
    private final IslandService islandService;
    private final LobbyService lobbyService;
    private final PlayerLandReserver landReserver;

    public IslandSetupListener(GameSetupSystem setupSystem, PlatformSetupAdapter setupAdapter, IslandService islandService, LobbyService lobbyService, PlayerLandReserver landReserver) {
        this.setupSystem = setupSystem;
        this.setupAdapter = setupAdapter;
        this.islandService = islandService;
        this.lobbyService = lobbyService;
        this.landReserver = landReserver;
    }

    @EventListener(state = ListeningState.MONITORING, ignoreCancelled = true)
    public void on(final StartIslandSetupEvent event) {
        IslandSetup islandSetup = event.islandSetup();
        IslandSetupData data = islandSetup.data();
        IslandSetupPlayer setupPlayer = data.player();

        setupPlayer.player().teleport(data.land().getIslandLocation());
        setupAdapter.onSetupPrepare(islandSetup, setupPlayer);
    }

    @EventListener(state = ListeningState.MONITORING, ignoreCancelled = true)
    public void on(final StopIslandSetupEvent event) {
        IslandSetup islandSetup = event.islandSetup();
        IslandSetupData setupData = islandSetup.data();
        if (!setupData.cancelled() && setupData.isReady()) {
            registerIsland(setupData);
        }

        IslandSetupPlayer setupPlayer = setupData.player();
        // this may not have been called by the setupSystem,
        // so we need to notify the system of the island stoppage
        setupSystem.cancelSetup(setupPlayer.player());

        landReserver.releaseSpot(setupPlayer.id());
        setupAdapter.onSetupStop(islandSetup, setupPlayer);
        setupPlayer.player().teleport(lobbyService.position());
    }

    private void registerIsland(IslandSetupData data) {
        try {
            islandService.register(data.slot(), data.origin(), data.schematicName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
