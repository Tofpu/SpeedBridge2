package com.github.tofpu.speedbridge2.bridge.setup.state;

import com.github.tofpu.speedbridge2.bridge.setup.IslandSetupData;
import com.github.tofpu.speedbridge2.bridge.setup.SetupPlayer;
import com.github.tofpu.speedbridge2.game.Game;
import com.github.tofpu.speedbridge2.game.land.LandController;
import com.github.tofpu.speedbridge2.game.state.StopGameState;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;

class StopSetupState extends StopGameState<IslandSetupData> {
    private final IslandService islandService;
    private final LobbyService lobbyService;
    private final LandController landController;

    StopSetupState(IslandService islandService, LobbyService lobbyService, LandController landController) {
        this.islandService = islandService;
        this.lobbyService = lobbyService;
        this.landController = landController;
    }
    @Override
    public void apply(Game<IslandSetupData> game) {
        IslandSetupData data = game.data();
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
