package com.github.tofpu.speedbridge2.bridge.game.state.core;

import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.bridge.game.state.CoreStateProvider;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeStartedState;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeStopState;
import com.github.tofpu.speedbridge2.lobby.LobbyService;

public class BridgeCoreStateProvider implements CoreStateProvider {
    private final GameAdapter gameAdapter;
    private final LobbyService lobbyService;

    public BridgeCoreStateProvider(GameAdapter gameAdapter, LobbyService lobbyService) {
        this.gameAdapter = gameAdapter;
        this.lobbyService = lobbyService;
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
        return new IslandStopStateState(lobbyService, gameAdapter);
    }
}
