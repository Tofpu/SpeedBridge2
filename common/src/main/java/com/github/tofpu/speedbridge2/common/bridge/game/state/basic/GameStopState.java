package com.github.tofpu.speedbridge2.common.bridge.game.state.basic;

import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGameData;
import com.github.tofpu.speedbridge2.common.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeStopState;
import com.github.tofpu.speedbridge2.common.game.land.LandController;
import com.github.tofpu.speedbridge2.common.game.Game;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;

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

        gameAdapter.onGameStop((IslandGame) game, gamePlayer);
        gamePlayer.getPlayer().sendMessage("Game ended within " + gameData.timerInSeconds() + " seconds, bravo!");
    }
}
