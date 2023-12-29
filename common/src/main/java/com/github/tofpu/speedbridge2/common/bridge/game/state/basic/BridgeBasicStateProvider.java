package com.github.tofpu.speedbridge2.common.bridge.game.state.basic;

import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.bridge.game.state.BasicStateProvider;
import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeStartedState;
import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeStopState;
import com.github.tofpu.speedbridge2.common.game.land.LandController;
import com.github.tofpu.speedbridge2.common.lobby.LobbyService;

public class BridgeBasicStateProvider implements BasicStateProvider {
    private final PlatformGameAdapter gameAdapter;
    private final LobbyService lobbyService;
    private final LandController landController;

    public  BridgeBasicStateProvider(PlatformGameAdapter gameAdapter, LobbyService lobbyService, LandController landController) {
        this.gameAdapter = gameAdapter;
        this.lobbyService = lobbyService;
        this.landController = landController;
    }

    @Override
    public BridgeGameState prepareState() {
        return new GamePrepareState(gameAdapter);
    }

    @Override
    public BridgeStartedState startedState() {
        return new GameStartedState();
    }

    @Override
    public BridgeStopState stopState() {
        return new GameStopState(lobbyService, gameAdapter, landController);
    }
}
