package com.github.tofpu.speedbridge2.common.setup.game.listener;

import com.github.tofpu.speedbridge2.common.gameextra.land.GameLandReserver;
import com.github.tofpu.speedbridge2.common.island.IslandService;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;
import com.github.tofpu.speedbridge2.common.setup.game.IslandSetupData;
import com.github.tofpu.speedbridge2.common.setup.game.SetupPlayer;
import com.github.tofpu.speedbridge2.common.setup.game.event.StopIslandSetupEvent;
import com.github.tofpu.speedbridge2.event.Listener;
import com.github.tofpu.speedbridge2.event.dispatcher.EventListener;
import com.github.tofpu.speedbridge2.event.dispatcher.ListeningState;

public class IslandSetupListener implements Listener {

    private final IslandService islandService;
    private final LobbyService lobbyService;
    private final GameLandReserver landReserver;

    public IslandSetupListener(IslandService islandService, LobbyService lobbyService, GameLandReserver landReserver) {
        this.islandService = islandService;
        this.lobbyService = lobbyService;
        this.landReserver = landReserver;
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
        landReserver.releaseSpot(setupPlayer.id());

        setupPlayer.player().teleport(lobbyService.position());
    }
}
