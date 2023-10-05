package com.github.tofpu.speedbridge2.bridge.setup;

import com.github.tofpu.speedbridge2.bridge.LandController;
import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.bridge.core.state.StopGameState;
import com.github.tofpu.speedbridge2.island.IslandService;
import com.github.tofpu.speedbridge2.lobby.LobbyService;

class EndSetupState extends StopGameState {
    private final IslandService islandService;
    private final LobbyService lobbyService;
    private final LandController landController;

    EndSetupState(IslandService islandService, LobbyService lobbyService, LandController landController) {
        this.islandService = islandService;
        this.lobbyService = lobbyService;
        this.landController = landController;
    }

    @Override
    public void apply(Game game) {
        apply((IslandSetup) game);
    }

    public void apply(IslandSetup game) {
        if (game.origin() == null) return;
        try {
            islandService.register(game.slot(), game.origin(), game.schematicName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        landController.releaseSpot(game.gamePlayer().id());

        SetupPlayer gamePlayer = (SetupPlayer) game.gamePlayer();
        gamePlayer.player().teleport(lobbyService.position());
    }
}
