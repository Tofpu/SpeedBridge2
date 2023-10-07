package com.github.tofpu.speedbridge2.bridge.game.state.custom;

import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.bridge.game.state.GameStateProvider;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;

public class BridgeGameStateProvider implements GameStateProvider {
    private final GameAdapter gameAdapter;
    private final EventDispatcherService eventDispatcherService;

    public BridgeGameStateProvider(GameAdapter gameAdapter, EventDispatcherService eventDispatcherService) {
        this.gameAdapter = gameAdapter;
        this.eventDispatcherService = eventDispatcherService;
    }

    @Override
    public BridgeGameState scoreState() {
        return new ScoredGameState(eventDispatcherService);
    }

    @Override
    public BridgeGameState resetState() {
        return new IslandResetGameState(gameAdapter);
    }
}
