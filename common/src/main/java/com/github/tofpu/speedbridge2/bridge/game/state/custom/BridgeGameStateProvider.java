package com.github.tofpu.speedbridge2.bridge.game.state.custom;

import com.github.tofpu.speedbridge2.GameAdapter;
import com.github.tofpu.speedbridge2.bridge.LandController;
import com.github.tofpu.speedbridge2.bridge.game.state.GameStateProvider;
import com.github.tofpu.speedbridge2.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.bridge.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import com.github.tofpu.speedbridge2.service.manager.ServiceManager;

public class BridgeGameStateProvider implements GameStateProvider {
    private final GameAdapter gameAdapter;
    private final EventDispatcherService eventDispatcherService;
    private final BridgeScoreService scoreService;

    public BridgeGameStateProvider(GameAdapter gameAdapter, EventDispatcherService eventDispatcherService, BridgeScoreService scoreService) {
        this.gameAdapter = gameAdapter;
        this.eventDispatcherService = eventDispatcherService;
        this.scoreService = scoreService;
    }

    @Override
    public BridgeGameState scoreState() {
        return new ScoredGameState(eventDispatcherService, scoreService);
    }

    @Override
    public BridgeGameState resetState() {
        return new IslandResetGameState(gameAdapter);
    }
}
