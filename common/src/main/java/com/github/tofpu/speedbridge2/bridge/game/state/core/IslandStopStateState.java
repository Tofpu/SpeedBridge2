package com.github.tofpu.speedbridge2.bridge.game.state.core;

import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.bridge.LandController;
import com.github.tofpu.speedbridge2.bridge.game.IslandGame;
import com.github.tofpu.speedbridge2.bridge.game.IslandGameHandler;
import com.github.tofpu.speedbridge2.bridge.game.IslandGamePlayer;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeStopState;
import com.github.tofpu.speedbridge2.lobby.LobbyService;

class IslandStopStateState extends BridgeStopState {
    private final LobbyService lobbyService;
    private final GameAdapter gameAdapter;

    public IslandStopStateState(LobbyService lobbyService, GameAdapter gameAdapter) {
        this.lobbyService = lobbyService;
        this.gameAdapter = gameAdapter;
    }

    @Override
    public void apply(IslandGameHandler handler, IslandGame game) {
        IslandGamePlayer gamePlayer = (IslandGamePlayer) game.gamePlayer();
        gamePlayer.getPlayer().teleport(lobbyService.position());

        handler.landController().releaseSpot(gamePlayer.id());

        gameAdapter.cleanGame(handler, game, gamePlayer);
        gamePlayer.getPlayer().sendMessage("Game ended within " + game.timerInSeconds() + " seconds, bravo!");
    }
}
