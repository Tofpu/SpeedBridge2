package com.github.tofpu.speedbridge2.bridge.game.state.basic;

import com.github.tofpu.speedbridge2.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.bridge.LandController;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeStopState;
import com.github.tofpu.speedbridge2.bridge.core.Game;
import com.github.tofpu.speedbridge2.lobby.LobbyService;

class GameStopState extends BridgeStopState {
    private final LobbyService lobbyService;
    private final PlatformGameAdapter gameAdapter;
    private final LandController landController;

    public GameStopState(LobbyService lobbyService, PlatformGameAdapter gameAdapter, LandController landController) {
        this.lobbyService = lobbyService;
        this.gameAdapter = gameAdapter;
        this.landController = landController;
    }

    @Override
    public void apply(Game<IslandGameData> game) {
        IslandGameData gameData = game.data();
        IslandGamePlayer gamePlayer = gameData.gamePlayer();

        gamePlayer.getPlayer().teleport(lobbyService.position());
        landController.releaseSpot(gamePlayer.id());

        gameAdapter.cleanGame((IslandGame) game, gamePlayer);
        gamePlayer.getPlayer().sendMessage("Game ended within " + gameData.timerInSeconds() + " seconds, bravo!");
    }
}
