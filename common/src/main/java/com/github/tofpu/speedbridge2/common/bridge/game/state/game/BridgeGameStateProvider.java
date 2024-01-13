package com.github.tofpu.speedbridge2.common.bridge.game.state.game;

import com.github.tofpu.speedbridge2.common.PlatformGameAdapter;
import com.github.tofpu.speedbridge2.common.bridge.game.state.GameStateHandler;
import com.github.tofpu.speedbridge2.common.bridge.game.state.GameStateProvider;
import com.github.tofpu.speedbridge2.common.bridge.game.state.generic.BridgeGameState;
import com.github.tofpu.speedbridge2.common.bridge.game.score.BridgeScoreService;
import com.github.tofpu.speedbridge2.event.dispatcher.EventDispatcherService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BridgeGameStateProvider implements GameStateProvider {
    private final GameStateHandler stateHandler;
    private final PlatformGameAdapter gameAdapter;
    private final BridgeScoreService scoreService;

    public static BridgeGameStateProvider.Builder newBuilder() {
        return new Builder();
    }

    private BridgeGameStateProvider(GameStateHandler stateHandler, PlatformGameAdapter gameAdapter, BridgeScoreService scoreService) {
        this.stateHandler = stateHandler;
        this.gameAdapter = gameAdapter;
        this.scoreService = scoreService;
    }

    @Override
    public BridgeGameState scoreState() {
        return new ScoredGameState(stateHandler, scoreService);
    }

    @Override
    public BridgeGameState resetState() {
        return new IslandResetGameState(stateHandler, gameAdapter);
    }

    public static class Builder {
        private PlatformGameAdapter gameAdapter;
        private BridgeScoreService scoreService;

        private Builder() {}

        public Builder setGameAdapter(PlatformGameAdapter gameAdapter) {
            this.gameAdapter = gameAdapter;
            return this;
        }

        public Builder setScoreService(BridgeScoreService scoreService) {
            this.scoreService = scoreService;
            return this;
        }

        public BridgeGameStateProvider build(@NotNull GameStateHandler stateHandler) {
            Objects.requireNonNull(gameAdapter);
            Objects.requireNonNull(scoreService);
            return new BridgeGameStateProvider(stateHandler, gameAdapter, scoreService);
        }
    }
}
